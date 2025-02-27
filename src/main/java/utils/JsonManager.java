package utils;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

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
    //Convierto Json a un objeto Java
    public static <T> T fromJson(String json, Class<T> clazz) {
        return gson.fromJson(json, clazz);
    }

    //convierte JSon a Map
    public static Map<String, Object> fromJsonToMap(String json) {
        Type type = new TypeToken<Map<String, Object>>() {}.getType();
        return gson.fromJson(json, type);
    }
}
