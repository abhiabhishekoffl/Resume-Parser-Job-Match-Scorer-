package com.resumematcher.config;

public class APIConfig {
    public static String getRapidApiKey() {
        String key = AppConfig.getProperty("api.rapidapi.key");
        if (key == null || key.isEmpty()) {
            // Return a placeholder indicating it's not set
            return "NOT_SET";
        }
        return key;
    }

    public static String getRapidApiHost() {
        return AppConfig.getProperty("api.rapidapi.host", "linkedin-data-api.p.rapidapi.com");
    }
}
