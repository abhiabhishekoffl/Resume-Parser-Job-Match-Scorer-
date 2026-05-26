package com.resumematcher.ui;

import com.resumematcher.config.AppConfig;

import javax.swing.*;
import java.awt.*;
import java.util.Properties;

public class SettingsDialog extends JDialog {
    
    private JTextField skillsWeightField;
    private JTextField expWeightField;
    private JTextField eduWeightField;
    private JTextField keywordWeightField;

    public SettingsDialog(JFrame parent) {
        super(parent, "Scoring Weights Settings", true);
        setSize(300, 250);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout(10, 10));
        
        JPanel grid = new JPanel(new GridLayout(4, 2, 5, 5));
        grid.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        grid.add(new JLabel("Skills Weight:"));
        skillsWeightField = new JTextField(AppConfig.getProperty("scoring.weight.skills"));
        grid.add(skillsWeightField);
        
        grid.add(new JLabel("Experience Weight:"));
        expWeightField = new JTextField(AppConfig.getProperty("scoring.weight.experience"));
        grid.add(expWeightField);
        
        grid.add(new JLabel("Education Weight:"));
        eduWeightField = new JTextField(AppConfig.getProperty("scoring.weight.education"));
        grid.add(eduWeightField);
        
        grid.add(new JLabel("Keyword Weight:"));
        keywordWeightField = new JTextField(AppConfig.getProperty("scoring.weight.keywords"));
        grid.add(keywordWeightField);
        
        add(grid, BorderLayout.CENTER);
        
        JPanel btnPanel = new JPanel();
        JButton saveBtn = new JButton("Save");
        saveBtn.addActionListener(e -> saveSettings());
        JButton cancelBtn = new JButton("Cancel");
        cancelBtn.addActionListener(e -> dispose());
        btnPanel.add(saveBtn);
        btnPanel.add(cancelBtn);
        
        add(btnPanel, BorderLayout.SOUTH);
    }
    
    private void saveSettings() {
        try {
            double sw = Double.parseDouble(skillsWeightField.getText());
            double ew = Double.parseDouble(expWeightField.getText());
            double edw = Double.parseDouble(eduWeightField.getText());
            double kw = Double.parseDouble(keywordWeightField.getText());
            
            AppConfig.saveProperty("scoring.weight.skills", String.valueOf(sw));
            AppConfig.saveProperty("scoring.weight.experience", String.valueOf(ew));
            AppConfig.saveProperty("scoring.weight.education", String.valueOf(edw));
            AppConfig.saveProperty("scoring.weight.keywords", String.valueOf(kw));
            
            JOptionPane.showMessageDialog(this, "Settings saved.");
            dispose();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid number format.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
