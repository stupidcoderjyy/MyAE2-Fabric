package com.stupidcoderx.modding.util;

import java.lang.reflect.Field;

public class ReflectionUtil {
    public static <T> T getObjectField(Object obj, String name) {
        return getObjectField(obj.getClass(), obj, name);
    }

    public static <T> T getObjectField(Class<?> parentClass, Object obj, String name) {
        try {
            return (T) getField(parentClass, name).get(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static Field getField(Class<?> clazz, String name) {
        try {
            Field f = clazz.getDeclaredField(name);
            f.setAccessible(true);
            return f;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
