# Integración con servicio externo de productos (PuntoRed / GestoPago)

## Objetivo
Consumir el endpoint externo `GET /sistema/service/getProductList.do` de la
API de PuntoRed, autenticado con Bearer Token, siguiendo la arquitectura por
capas ya usada en el proyecto.

## Hallazgo clave durante el desarrollo
La documentación oficial de PuntoRed reveló restricciones que no eran
evidentes en el enunciado original:
- La respuesta es **XML**, no JSON.
- El endpoint solo puede invocarse **hasta 3 veces al día**; de lo contrario
  la IP queda bloqueada.
- La documentación indica explícitamente que este endpoint **no debe usarse
  como fuente en vivo** para el frontend, sino que su resultado debe
  guardarse en base de datos propia.

Estas restricciones cambiaron el diseño de "llamar en vivo en cada request"
a un modelo de **sincronización programada + caché en base de datos**.

## Arquitectura implementada

- **`ProductoServiceClient`** (Feign): hace la llamada HTTP GET al servicio
  externo. Recibe el Bearer Token y un `X-API-Key` opcional como headers.
  La respuesta XML se decodifica automáticamente a `ProductListResponse`
  mediante JAXB (ya presente en las dependencias del proyecto).

- **`ProductoService` / `ProductoServiceImpl`**: obtiene el token vigente
  reutilizando la infraestructura ya existente (`GestoPagoTokenService`,
  que renueva el JWT cada 24h como exige la API), invoca al Client, y
  traduce cualquier error técnico de Feign a `ProductoServiceException`.

- **`ProductoSyncService` / `ProductoSyncServiceImpl`**: orquestador
  programado (`@Scheduled`, una vez al día a las 3 AM, configurable vía
  `product-service.sync-cron`) que:
  1. Consulta el servicio externo a través de `ProductoService`.
  2. Guarda un snapshot completo de la respuesta en **MongoDB**
     (colección `productos_snapshot`), útil para auditoría/histórico.
  3. Delega el guardado en **PostgreSQL** a `ProductoPersistenceService`.

- **`ProductoPersistenceService` / `ProductoPersistenceServiceImpl`**:
  guarda o actualiza cada producto en PostgreSQL (tabla `productos`) de
  forma **asíncrona** (`@Async`), para no bloquear la sincronización
  diaria. Usa `idServicio` + `idProducto` como clave de upsert (evita
  duplicados en corridas sucesivas).

- **`ProductoController`**: expone `GET /productos` leyendo directamente
  de PostgreSQL — nunca llama al servicio externo en vivo, cumpliendo la
  restricción de la documentación oficial.

## Decisiones técnicas

- **Token:** se reutiliza el flujo de autenticación ya existente en el
  proyecto (`GestoPagoAuthClient` + `GestoPagoTokenServiceImpl`), en vez
  de introducir un token estático, porque la API exige un JWT renovado
  cada 24h y prohíbe pedir uno nuevo por cada llamada.

- **Manejo de errores:** `ProductoServiceException` traduce los distintos
  fallos de Feign (`Unauthorized`/`Forbidden` → autenticación,
  `RetryableException` → timeout, `FeignException` genérico → respuesta