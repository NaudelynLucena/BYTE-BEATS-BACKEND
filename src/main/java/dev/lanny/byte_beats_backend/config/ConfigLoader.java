package dev.lanny.byte_beats_backend.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigLoader {
    private static Properties properties = new Properties();
    static {
        try (InputStream input = ConfigLoader.class.getClassLoader().getResourceAsStream("config.properties")) {
            if (input == null) {
                throw new RuntimeException("El archivo config.properties no fue encontrado en el classpath.");
            }
            properties.load(input);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Error loading configuration file: config.properties");
        }
    }
    public static String get(String key) {
        return properties.getProperty(key, "");
    }
}
