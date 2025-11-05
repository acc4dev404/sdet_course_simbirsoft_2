package com.simbirsoft.utilities;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Провайдер параметров конфигурации.
 * Загружает параметры из properties-файлов и предоставляет к ним доступ.
 * Реализован как Singleton.
 */
public final class ParameterProvider {

    /** Путь к файлу конфигурации */
    private static final String PARAMETERS_PATH = "config.properties";

    /** Единственный экземпляр класса */
    private static ParameterProvider instance;

    /** Коллекция параметров конфигурации */
    private final Map<String, String> parameters;

    /**
     * Приватный конструктор для реализации Singleton.
     * Загружает параметры из файла конфигурации.
     */
    private ParameterProvider() {
        try {
            parameters = new HashMap<>();
            Properties prop = new Properties();
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream(PARAMETERS_PATH);
            prop.load(inputStream);
            prop.stringPropertyNames()
                    .forEach( key -> parameters.put(key, prop.getProperty(key)));
        } catch (IOException e) {
            throw new RuntimeException("Ошибка загрузки файла конфигурации: " + PARAMETERS_PATH, e);
        }
    }

    /**
     * Возвращает значение параметра по ключу.
     *
     * @param key ключ параметра
     * @return значение параметра
     */
    public static String get(String key) {
        if (instance == null) {
            instance = new ParameterProvider();
        }
        return instance.parameters.get(key);
    }
}