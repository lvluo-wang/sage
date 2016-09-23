package me.icymint.sage.base.core.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import me.icymint.sage.base.spec.exception.Exceptions;

import java.io.IOException;

/**
 * Created by daniel on 2016/9/23.
 */
public class Jsons {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static String toJson(Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw Exceptions.wrap(e);
        }
    }

    public static <E> E fromJson(String json, Class<E> clazz) {
        try {
            return objectMapper.readValue(json, clazz);
        } catch (IOException e) {
            throw Exceptions.wrap(e);
        }
    }
}
