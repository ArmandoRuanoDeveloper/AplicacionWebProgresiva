package com.proyecto.servicios.model.onboarding;

public final class Reglas {
    private Reglas() {}

    // Letras (con acentos y ñ), una sola palabra separada por UN espacio, sin espacios al inicio/fin
    public static final String NOMBRE = "^[\\p{L}&&\\p{IsLatin}]+( [\\p{L}&&\\p{IsLatin}]+)*$";
    // Igual, pero también dígitos y . , # / & ' -   (calle, colonia, empresa)
    public static final String TEXTO = "^[[\\p{L}&&\\p{IsLatin}]\\d.,#/&'-]+( [[\\p{L}&&\\p{IsLatin}]\\d.,#/&'-]+)*$";
    // 10, 10-A, S/N
    public static final String NUMERO = "^[A-Za-z0-9]+([/-][A-Za-z0-9]+)*$";
    public static final String CURP = "^[A-Z][AEIOU][A-Z]{2}\\d{2}(0[1-9]|1[0-2])(0[1-9]|[12]\\d|3[01])[HM][A-Z]{2}[BCDFGHJKLMNÑPQRSTVWXYZ]{3}[A-Z\\d][0-9]$";
    public static final String RFC = "^[A-ZÑ&]{3,4}\\d{2}(0[1-9]|1[0-2])(0[1-9]|[12]\\d|3[01])[A-Z0-9]{3}$";
    public static final String CORREO = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*\\.[A-Za-z]{2,}$";
    public static final String TELEFONO = "\\d{10}";
    public static final String CP = "\\d{5}";
    // ASCII imprimible, sin espacios, 8-72, mayúscula + minúscula + número + especial
    public static final String PASSWORD = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!-/:-@\\[-`{-~])[!-~]{8,72}$";
}