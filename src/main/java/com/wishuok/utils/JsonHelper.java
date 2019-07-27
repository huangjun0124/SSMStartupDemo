package com.wishuok.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public class JsonHelper {

    public static String toJson(Object model){
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(model);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            LogHelper.LogError(e.toString());
        }
        return null;
    }

    public Object toObject(String json, Class claz){
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(json, claz);
        } catch (IOException e) {
            e.printStackTrace();
            LogHelper.LogError(e.toString());
        }
        return null;
    }
}
