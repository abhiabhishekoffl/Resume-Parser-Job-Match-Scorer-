package com.resumematcher.ui;

import com.resumematcher.model.JobDescription;
import com.resumematcher.model.Resume;
import com.resumematcher.model.ScanResult;
import com.resumematcher.parser.DOCXParser;
import com.resumematcher.parser.PDFParser;
import com.resumematcher.scorer.ScoringEngine;
import com.resumematcher.nlp.SectionDetector;
import com.resumematcher.nlp.ExperienceCalculator;
import java.util.Map;

import javax.swing.*;
import java.awt.*;
import java.io.File;

public class UploadPanel extends JPanel {
    private MainFrame mainFrame;
    private File selectedResumeFile;
    private JLabel fileLabel;
    private JTextArea jdTextArea;
    
    public UploadPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        setLayout(new BorderLayout(20, 20));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JPanel uploadArea = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton uploadBtn = new JButton("Select Resume (PDF/DOCX)");
        fileLabel = new JLabel("No file selected.");
        
        uploadBtn.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            int res = chooser.showOpenDialog(this);
            if (res == JFileChooser.APPROVE_OPTION) {
                selectedResumeFile = chooser.getSelectedFile();
                fileLabel.setText("Selected: " + selectedResumeFile.getName());
            }
        });
        
        uploadArea.add(uploadBtn);
        uploadArea.add(fileLabel);
        
        JPanel jdArea = new JPanel(new BorderLayout(5, 5));
        jdArea.add(new JLabel("Paste Job Description Here:"), BorderLayout.NORTH);
        jdTextArea = new JTextArea();
        jdTextArea.setLineWrap(true);
        jdTextArea.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(jdTextArea);
        jdArea.add(scrollPane, BorderLayout.CENTER);
        
        JButton analyzeBtn = new JButton("Analyze Match");
        analyzeBtn.setFont(new Font("SansSerif", Font.BOLD, 16));
        analyzeBtn.addActionListener(e -> analyze());
        
        add(uploadArea, BorderLayout.NORTH);
        add(jdArea, BorderLayout.CENTER);
        add(analyzeBtn, BorderLayout.SOUTH);
    }
    
    private void analyze() {
        if (selectedResumeFile == null || !selectedResumeFile.exists()) {
            JOptionPane.showMessageDialog(this, "Please select a valid resume file.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        String jdText = jdTextArea.getText().trim();
        if (jdText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please paste a job description.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        
        try {
            String resumeText = "";
            String fileName = selectedResumeFile.getName().toLowerCase();
            
            if (fileName.endsWith(".pdf")) {
                resumeText = new PDFParser().extractText(selectedResumeFile);
            } else if (fileName.endsWith(".docx")) {
                resumeText = new DOCXParser().extractText(selectedResumeFile);
            } else {
                JOptionPane.showMessageDialog(this, "Unsupported file format.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            Resume resume = new Resume();
            resume.setFilename(selectedResumeFile.getName());
            resume.setFilepath(selectedResumeFile.getAbsolutePath());
            resume.setExtractedText(resumeText);
            
            SectionDetector sectionDetector = new SectionDetector();
            Map<String, String> sections = sectionDetector.detectSections(resumeText);
            
            String expText = sections.getOrDefault("EXPERIENCE", resumeText); // fallback to full text if no explicit section
            ExperienceCalculator expCalculator = new ExperienceCalculator();
            resume.setExperienceYears(expCalculator.calculateYearsExperience(expText));
            
            JobDescription jd = new JobDescription();
            jd.setDescriptionText(jdText);
            
            ScoringEngine engine = new ScoringEngine();
            ScanResult result = engine.computeScore(resume, jd);
            
            mainFrame.getResultsDashboard().updateResults(result, resume);
            mainFrame.showResults();
            
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "An error occurred during analysis.", "Error", JOptionPane.ERROR_MESSAGE);
        } finally {
            setCursor(Cursor.getDefaultCursor());
        }
    }
}
