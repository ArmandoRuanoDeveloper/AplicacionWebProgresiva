# API 

API REST que permite dar de alta a clientes personas físicas de una institución financiera.
Al registrar un cliente, el sistema crea automáticamente su cuenta y su usuario de acceso, y ofrece consultas, actualización y baja lógica, con validaciones obligatorias en todos los campos.

# Tecnologías
Java 17 y Spring Boot 3.3.6 (Gradle)
Spring Web, Spring Data JPA (Hibernate 6.5) y Bean Validation
Spring Security con autenticación JWT (jjwt) y cifrado de contraseñas con BCrypt
PostgreSQL con migraciones versionadas con Flyway
springdoc-openapi (Swagger UI)
JUnit 5 y Mockito (pruebas unitarias), Postman y Apache JMeter (pruebas funcionales y de carga)
Docker y Render (despliegue)

# Diagrama de base de datos
<img width="1022" height="1046" alt="er_onboarding drawio" src="https://github.com/user-attachments/assets/7c67869b-b94c-4ee7-9fc5-17654d358802" />

(Los scripts de base de datos están disponibles en la ruta: "src/main/resources/db/migration")
