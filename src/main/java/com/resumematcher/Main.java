package com.resumematcher;

import com.formdev.flatlaf.FlatDarkLaf;
import com.resumematcher.config.AppConfig;
import com.resumematcher.db.DatabaseManager;
import com.resumematcher.ui.MainFrame;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class Main {
    public static void main(String[] args) {
        System.out.println("Initializing Resume Matcher...");
        
        // 1. Load configuration (creates files/folders if they don't exist)
        AppConfig.loadConfig();
        System.out.println("Configuration loaded. DB path: " + AppConfig.getProperty("db.path"));
        
        // 2. Initialize database (creates tables and seeds data if empty)
        DatabaseManager.initializeDatabase();
        System.out.println("Database initialized successfully.");
        
        System.out.println("Ready!");
        // Future phases will launch the Swing UI here.
        // 3. Set FlatLaf Look and Feel and Start UI
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(new FlatDarkLaf());
            } catch (Exception ex) {
                System.err.println("Failed to initialize LaF");
            }
            MainFrame frame = new MainFrame();
            frame.setVisible(true);
        });
    }
}
