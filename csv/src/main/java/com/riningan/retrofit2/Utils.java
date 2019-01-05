package com.riningan.retrofit2;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;


class Utils {
    private static final char BACKSLASH = '\\';
    private static final char COMMA = ',';
    private static final char QUOTATION = '"';


    static ArrayList<String> getValues(String row) {
        row = row.trim();
        ArrayList<String> result = new ArrayList<>();
        StringBuilder value = new StringBuilder();
        boolean open = false;
        boolean backslash = false;
        for (int i = 0; i < row.length(); i++) {
            char cur = row.charAt(i);
            switch (cur) {
                case BACKSLASH:
                    backslash = true;
                    break;
                case QUOTATION:
                    if (backslash) {
                        value.append(cur);
                        backslash = false;
                    } else {
                        open = !open;
                    }
                    break;
                case COMMA:
                    if (backslash) {
                        value.append(BACKSLASH);
                        backslash = false;
                    }
                    if (open) {
                        value.append(cur);
                    } else {
                        result.add(value.toString());
                        value = new StringBuilder();
                    }
                    break;
                default:
                    if (backslash) {
                        value.append(BACKSLASH);
                        backslash = false;
                    }
                    value.append(cur);
                    break;
            }
        }
        if (backslash) {
            value.append(BACKSLASH);
        }
        result.add(value.toString());
        return result;
    }

    static String getTitles(Object object) {
        Class cls = object.getClass();
        StringBuilder stringBuilder = new StringBuilder();
        for (Field field : cls.getDeclaredFields()) {
            stringBuilder.append(COMMA).append(field.getName());
        }
        return RemoveFirstComma(stringBuilder);
    }

    static String getRow(Object object) throws IllegalAccessException {
        Class cls = object.getClass();
        StringBuilder stringBuilder = new StringBuilder();
        for (Field field : cls.getDeclaredFields()) {
            stringBuilder.append(COMMA);
            Type type = field.getType();
            if (type == boolean.class || type == Boolean.class) {
                stringBuilder.append(String.valueOf(field.getBoolean(object)));
            } else if (type == byte.class || type == Byte.class) {
                stringBuilder.append(String.valueOf(field.getByte(object)));
            } else if (type == char.class || type == Character.class) {
                stringBuilder.append(String.valueOf(field.getChar(object)));
            } else if (type == double.class || type == Double.class) {
                stringBuilder.append(String.valueOf(field.getDouble(object)));
            } else if (type == float.class || type == Float.class) {
                stringBuilder.append(String.valueOf(field.getFloat(object)));
            } else if (type == long.class || type == Long.class) {
                stringBuilder.append(String.valueOf(field.getFloat(object)));
            } else if (type == int.class || type == Integer.class) {
                stringBuilder.append(String.valueOf(field.getInt(object)));
            } else if (type == short.class || type == Short.class) {
                stringBuilder.append(String.valueOf(field.getShort(object)));
            } else if (type == String.class) {
                stringBuilder.append(normalize((String) field.get(object)));
            } else if (type == Date.class) {
                stringBuilder.append(normalize(field.get(object).toString()));
            } else {
                stringBuilder.append(normalize(field.get(object).toString()));
            }
        }
        return RemoveFirstComma(stringBuilder);
    }


    private static String normalize(String str) {
        return QUOTATION + str + QUOTATION;
    }

    private static String RemoveFirstComma(StringBuilder stringBuilder) {
        String result = stringBuilder.toString();
        if (result.length() > 0) {
            result = result.substring(1);
        }
        return result;
    }
}