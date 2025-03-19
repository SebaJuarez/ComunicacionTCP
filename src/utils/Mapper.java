package utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public class Mapper {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    static {
        objectMapper.registerModule(new JavaTimeModule()); // Habilita soporte para LocalDateTime
    }

    public static ObjectMapper getInstance() {
        return objectMapper;
    }

    public static String toJson(Mensaje mensaje) throws JsonProcessingException {
        return objectMapper.writeValueAsString(mensaje);
    }

    public static Mensaje fromJson(String json) throws JsonMappingException, JsonProcessingException {
        return objectMapper.readValue(json, Mensaje.class);
    }
}

