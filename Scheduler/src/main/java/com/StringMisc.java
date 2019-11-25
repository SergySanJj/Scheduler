package com;

public class StringMisc {
    public static String form(String string, int len) {
        StringBuilder res = new StringBuilder(string);
        while (res.length() < len) {
            res.append(" ");
        }
        return res.toString();
    }

    public static String form(String string) {
        return form(string, 8);
    }

    public static String form(int a){
        return form(Integer.toString(a));
    }
}
