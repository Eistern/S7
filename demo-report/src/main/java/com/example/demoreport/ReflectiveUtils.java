package com.example.demoreport;

import org.springframework.stereotype.Service;

import java.lang.reflect.Field;

@Service
public class ReflectiveUtils {
    public static Object getValueByName(Object subject, String fieldName) throws NoSuchFieldException, IllegalAccessException {
        Class<?> workingClass = subject.getClass();
        Field field = workingClass.getDeclaredField(fieldName);
        field.setAccessible(true);
        return field.get(subject);
    }
}
