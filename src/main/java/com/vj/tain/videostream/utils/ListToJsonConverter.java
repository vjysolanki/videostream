package com.vj.tain.videostream.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.ArrayList;
import java.util.List;

@Converter
@Slf4j
public class ListToJsonConverter implements AttributeConverter<List<String>, String> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(List<String> attribute) {
        if (attribute == null || attribute.isEmpty()) {
            log.warn("[CONVERTER] - Missing attribute while converting fields to db column");
            return null;
        }
        try {
            return objectMapper.writeValueAsString(attribute);
        } catch (JsonProcessingException e) {
            log.error("[CONVERTER] - Error converting list to JSON " + e.getMessage()) ;
            throw new RuntimeException("Error converting list to JSON", e);
        }
    }

    @Override
    public List<String> convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.isBlank()) {
            log.warn("[CONVERTER] - Missing dbData while converting fields to entity attribute");
            return new ArrayList<>();
        }
        try {
            return objectMapper.readValue(dbData, List.class);
        } catch (JsonProcessingException e) {
            log.error("[CONVERTER] - Error converting JSON to list " + e.getMessage()) ;
            throw new RuntimeException("Error converting JSON to list", e);
        }
    }
}
