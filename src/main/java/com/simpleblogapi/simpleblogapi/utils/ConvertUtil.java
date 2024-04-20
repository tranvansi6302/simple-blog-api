package com.simpleblogapi.simpleblogapi.utils;

public class ConvertUtil {
    public static String convertToSnakeCase(String camelCase) {
        return camelCase.replaceAll("([a-z])([A-Z]+)", "$1_$2").toLowerCase();
    }
}
