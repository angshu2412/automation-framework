package com.qa.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigReader {

	private static ConfigReader configReaderObj;
	
	private Properties props;
	
	private ConfigReader() {
        try {
            InputStream configFile = getClass()
                .getClassLoader()
                .getResourceAsStream("config.properties");

            if (configFile == null) {
                throw new RuntimeException("config.properties not found");
            }

            props = new Properties();
            props.load(configFile);

        } catch (IOException e) {
            throw new RuntimeException("Failed to load config.properties", e);
        }
    }
	
	public static ConfigReader getInstance()
	{
		if (configReaderObj == null) {
			configReaderObj = new ConfigReader();
	    }
	    return configReaderObj;
	}
	
	public String get(String key) {
        if (!props.containsKey(key)) {
            throw new RuntimeException("Invalid config key: " + key);
        }
        return props.getProperty(key);
    }
	
}
