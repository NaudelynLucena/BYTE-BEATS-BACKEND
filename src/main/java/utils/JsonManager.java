package utils;

import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class JsonManager {
    private static final Gson gson = new Gson();

    // cOnvertir unobjeto a Json
    public static <T> String toJson(T obj) {
        return gson.toJson(obj);
    }
    // convertir el Json a una lista de objetos
    public static <T>  List <T> fromJsonList(String json, Class<T> clazz) {
        return gson.fromJson(json, TypeToken.getParameterized(List.class, clazz).getType());
    }
}
