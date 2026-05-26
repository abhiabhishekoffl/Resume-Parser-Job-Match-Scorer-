package com.resumematcher.config;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

public class AppConfig {
    private static final String APP_DIR = System.getProperty("user.home") + File.separator + ".resumematcher";
    private static final String CONFIG_FILE = APP_DIR + File.separator + "config.properties";
    private static Properties properties;

    public static void loadConfig() {
        properties = new Properties();
        File dir = new File(APP_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        File configFile = new File(CONFIG_FILE);
        if (configFile.exists()) {
            try (InputStream input = new FileInputStream(configFile)) {
                properties.load(input);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        
        // Set defaults if not present
        setDefault("db.path", APP_DIR + File.separator + "resumematcher.db");
        setDefault("uploads.dir", APP_DIR + File.separator + "uploads");
        setDefault("reports.dir", APP_DIR + File.separator + "reports");
        setDefault("api.rapidapi.key", "");
        setDefault("api.rapidapi.host", "jsearch.p.rapidapi.com");
        setDefault("smtp.host", "smtp.gmail.com");
        setDefault("smtp.port", "587");
        setDefault("smtp.username", "");
        setDefault("smtp.password", "");
        setDefault("ui.theme", "light");
        setDefault("scoring.weight.skills", "0.50");
        setDefault("scoring.weight.experience", "0.25");
        setDefault("scoring.weight.education", "0.15");
        setDefault("scoring.weight.keywords", "0.10");

        saveConfig();
        
        // Ensure upload and report directories exist
        try {
            Files.createDirectories(Paths.get(getProperty("uploads.dir")));
            Files.createDirectories(Paths.get(getProperty("reports.dir")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void setDefault(String key, String value) {
        if (!properties.containsKey(key)) {
            properties.setProperty(key, value);
        }
    }

    public static void saveConfig() {
        try (OutputStream output = new FileOutputStream(CONFIG_FILE)) {
            properties.store(output, "Resume Matcher Configuration");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static String getProperty(String key) {
        return properties.getProperty(key);
    }
    
    public static String getProperty(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }
    
    public static void setProperty(String key, String value) {
        properties.setProperty(key, value);
    }
    
    public static void saveProperty(String key, String value) {
        properties.setProperty(key, value);
        saveConfig();
    }
}
