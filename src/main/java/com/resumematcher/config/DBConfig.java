package com.resumematcher.config;

public class DBConfig {
    public static String getJdbcUrl() {
        return "jdbc:sqlite:" + AppConfig.getProperty("db.path");
    }
}
