package org.ulpgc.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Properties;

public class ConfigLoader {
    private final Properties properties;

    public ConfigLoader(String fileName) {
        properties = new Properties();
        try (InputStream input = getClass().getClassLoader().getResourceAsStream(fileName)) {
            if (input == null) {
                throw new IllegalArgumentException("Config file not found: " + fileName);
            }
            properties.load(input);
        } catch (IOException ex) {
            throw new RuntimeException("Error loading config file: " + fileName, ex);
        }
    }

    public int[] getIntArray(String key) {
        return Arrays.stream(getProperty(key).split(","))
                .map(String::trim)
                .mapToInt(Integer::parseInt)
                .toArray();
    }

    public double[] getDoubleArray(String key) {
        return Arrays.stream(getProperty(key).split(","))
                .map(String::trim)
                .mapToDouble(Double::parseDouble)
                .toArray();
    }

    public String getProperty(String key) {
        String value = properties.getProperty(key);
        if (value == null) {
            throw new IllegalArgumentException("Key '" + key + "' not found in config properties");
        }
        return value;
    }

    public int getInt(String key) {
        String value = getProperty(key);
        return Integer.parseInt(value.trim());
    }
}
