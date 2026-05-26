package com.resumematcher.ui;

import com.formdev.flatlaf.FlatDarkLaf;
import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    private CardLayout cardLayout;
    private JPanel mainPanel;
    
    private UploadPanel uploadPanel;
    private ResultsDashboard resultsDashboard;
    
    public MainFrame() {
        setTitle("Resume Parser & Job Match Scorer");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        setupMenu();
        
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        
        uploadPanel = new UploadPanel(this);
        resultsDashboard = new ResultsDashboard(this);
        
        mainPanel.add(uploadPanel, "UPLOAD");
        mainPanel.add(resultsDashboard, "RESULTS");
        
        add(mainPanel, BorderLayout.CENTER);
        
        cardLayout.show(mainPanel, "UPLOAD");
    }
    
    private void setupMenu() {
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        JMenuItem settingsItem = new JMenuItem("Settings...");
        settingsItem.addActionListener(e -> {
            new SettingsDialog(this).setVisible(true);
        });
        JMenuItem exitItem = new JMenuItem("Exit");
        exitItem.addActionListener(e -> System.exit(0));
        
        fileMenu.add(settingsItem);
        fileMenu.addSeparator();
        fileMenu.add(exitItem);
        menuBar.add(fileMenu);
        setJMenuBar(menuBar);
    }
    
    public void showResults() {
        cardLayout.show(mainPanel, "RESULTS");
    }
    
    public void showUpload() {
        cardLayout.show(mainPanel, "UPLOAD");
    }

    public ResultsDashboard getResultsDashboard() {
        return resultsDashboard;
    }
}
