package com.bimbiya.server.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.UUID;

/**
 * @author Hasintha_S
 * @date 1/13/2024.
 */

public class Utility
{
    private static SimpleDateFormat simpleDateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");

    public static Date getSystemDate()
    {
        return new Date();
    }

    public static String getBase64Decoder(String encodedString)
    {
        byte[] decodedBytes = Base64.getDecoder().decode(encodedString);
        return new String(decodedBytes);
    }

    public static String getBase64Encoder(String stringToEncode)
    {
        return Base64.getEncoder().encodeToString(stringToEncode.getBytes());
    }

    public static String getBase64Encoder(Object object)
    {
        return Base64.getEncoder().encodeToString(generateJsonString(object).getBytes());
    }

    public static UUID generateType4UUID()
    {
        return UUID.randomUUID();
    }


    public static <T> String generateJsonString(T t) {
        String jsonString;
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
            jsonString = objectMapper.writeValueAsString(t);
        } catch (JsonProcessingException e) {
            jsonString = null;
        }
        return jsonString;
    }


}
