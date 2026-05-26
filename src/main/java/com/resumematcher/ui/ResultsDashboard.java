package com.resumematcher.ui;

import com.resumematcher.api.EmailNotifier;
import com.resumematcher.export.ReportGenerator;
import com.resumematcher.model.Resume;
import com.resumematcher.model.ScanResult;
import com.resumematcher.model.Skill;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.stream.Collectors;

public class ResultsDashboard extends JPanel {
    private MainFrame mainFrame;
    private JLabel scoreLabel;
    private JTextArea recommendationsArea;
    private JTextArea missingSkillsArea;
    private JPanel chartPanelContainer;

    private ScanResult currentResult;
    private Resume currentResume;

    public ResultsDashboard(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        setLayout(new BorderLayout(15, 15));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JPanel headerPanel = new JPanel(new BorderLayout());
        JButton backBtn = new JButton("<- Back");
        backBtn.addActionListener(e -> mainFrame.showUpload());
        headerPanel.add(backBtn, BorderLayout.WEST);
        
        scoreLabel = new JLabel("Match Score: --%", SwingConstants.CENTER);
        scoreLabel.setFont(new Font("SansSerif", Font.BOLD, 28));
        headerPanel.add(scoreLabel, BorderLayout.CENTER);
        
        add(headerPanel, BorderLayout.NORTH);

        // Center Panel for Chart and Info
        JPanel centerPanel = new JPanel(new GridLayout(1, 2, 10, 10));
        
        chartPanelContainer = new JPanel(new BorderLayout());
        centerPanel.add(chartPanelContainer);

        JPanel infoPanel = new JPanel(new GridLayout(2, 1, 10, 10));
        
        JPanel missingPanel = new JPanel(new BorderLayout());
        missingPanel.add(new JLabel("Missing Skills:"), BorderLayout.NORTH);
        missingSkillsArea = new JTextArea();
        missingSkillsArea.setEditable(false);
        missingSkillsArea.setLineWrap(true);
        missingPanel.add(new JScrollPane(missingSkillsArea), BorderLayout.CENTER);
        
        JPanel recPanel = new JPanel(new BorderLayout());
        recPanel.add(new JLabel("Recommendations:"), BorderLayout.NORTH);
        recommendationsArea = new JTextArea();
        recommendationsArea.setEditable(false);
        recommendationsArea.setLineWrap(true);
        recPanel.add(new JScrollPane(recommendationsArea), BorderLayout.CENTER);

        infoPanel.add(missingPanel);
        infoPanel.add(recPanel);

        centerPanel.add(infoPanel);
        add(centerPanel, BorderLayout.CENTER);

        // Actions Panel
        JPanel actionsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        JButton exportBtn = new JButton("Export PDF");
        JButton emailBtn = new JButton("Email Report");
        JButton jobsBtn = new JButton("Find Jobs (LinkedIn)");

        exportBtn.addActionListener(e -> exportPDF());
        emailBtn.addActionListener(e -> emailReport());
        jobsBtn.addActionListener(e -> findJobs());

        actionsPanel.add(exportBtn);
        actionsPanel.add(emailBtn);
        actionsPanel.add(jobsBtn);

        add(actionsPanel, BorderLayout.SOUTH);
    }

    public void updateResults(ScanResult result, Resume resume) {
        this.currentResult = result;
        this.currentResume = resume;
        
        scoreLabel.setText(String.format("Match Score: %.1f%%", result.getTotalScore()));
        
        String missing = result.getMissingSkills().stream()
                .map(Skill::getName)
                .collect(Collectors.joining(", "));
        missingSkillsArea.setText(missing.isEmpty() ? "None! Perfect Match." : missing);
        
        String recs = String.join("\n- ", result.getRecommendations());
        recommendationsArea.setText("- " + recs);

        // Update Chart
        chartPanelContainer.removeAll();
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        dataset.addValue(result.getSkillsScore(), "Score", "Skills");
        dataset.addValue(result.getExperienceScore(), "Score", "Experience");
        dataset.addValue(result.getEducationScore(), "Score", "Education");
        dataset.addValue(result.getKeywordScore(), "Score", "Keywords");

        JFreeChart chart = ChartFactory.createBarChart(
                "Sub-Scores Breakdown",
                "Category",
                "Score (0-100)",
                dataset,
                PlotOrientation.VERTICAL,
                false, true, false
        );

        ChartPanel cp = new ChartPanel(chart);
        chartPanelContainer.add(cp, BorderLayout.CENTER);
        chartPanelContainer.revalidate();
        chartPanelContainer.repaint();
    }

    private void exportPDF() {
        if (currentResult == null || currentResume == null) return;
        ReportGenerator generator = new ReportGenerator();
        File pdf = generator.generatePDFReport(currentResult, currentResume);
        if (pdf != null && pdf.exists()) {
            JOptionPane.showMessageDialog(this, "PDF Exported successfully to:\n" + pdf.getAbsolutePath());
        } else {
            JOptionPane.showMessageDialog(this, "Failed to generate PDF.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void emailReport() {
        if (currentResult == null || currentResume == null) return;
        String email = JOptionPane.showInputDialog(this, "Enter destination email address:");
        if (email != null && !email.trim().isEmpty()) {
            mainFrame.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            SwingWorker<Boolean, Void> worker = new SwingWorker<>() {
                @Override
                protected Boolean doInBackground() {
                    ReportGenerator generator = new ReportGenerator();
                    File pdf = generator.generatePDFReport(currentResult, currentResume);
                    EmailNotifier notifier = new EmailNotifier();
                    return notifier.sendResultEmail(email, currentResult, pdf);
                }

                @Override
                protected void done() {
                    mainFrame.setCursor(Cursor.getDefaultCursor());
                    try {
                        boolean success = get();
                        if (success) {
                            JOptionPane.showMessageDialog(ResultsDashboard.this, "Email sent (or mocked successfully)!");
                        } else {
                            JOptionPane.showMessageDialog(ResultsDashboard.this, "Failed to send email.", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            };
            worker.execute();
        }
    }

    private void findJobs() {
        if (currentResult == null) return;
        
        // Pick a keyword based on missing skills or generic fallback
        String keyword = "Software Engineer";
        if (!currentResult.getMissingSkills().isEmpty()) {
            keyword = currentResult.getMissingSkills().get(0).getName() + " Developer";
        } else if (currentResume.getSkills() != null && !currentResume.getSkills().isEmpty()) {
            keyword = currentResume.getSkills().get(0) + " Developer";
        }
        
        JobsDialog dialog = new JobsDialog(mainFrame, keyword);
        dialog.setVisible(true);
    }
}
