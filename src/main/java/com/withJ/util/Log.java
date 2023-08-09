package com.withJ.util;

public class Log {

    private static int number = 1;

    private Log() {
    }

    public static void printClass(Class<?> c) {
        defaultPrint("call " + c.getSimpleName());
    }

    public static void printVariable(String name, Object value) {
        System.out.println(name + " : " + value);
    }

    public static void print(String message) {
        defaultPrint(message);
    }

    private static void defaultPrint(String message) {
        System.out.println(number++ + " ========================================");
        System.out.println(message);
    }
}
