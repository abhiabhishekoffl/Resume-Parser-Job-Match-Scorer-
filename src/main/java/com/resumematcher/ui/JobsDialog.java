package com.resumematcher.ui;

import com.resumematcher.api.LinkedInJobsAPI;
import com.resumematcher.model.JobDescription;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class JobsDialog extends JDialog {

    public JobsDialog(JFrame parent, String keyword) {
        super(parent, "Recommended Jobs for: " + keyword, true);
        setSize(800, 500);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());

        String[] columns = {"Job Title", "Company", "Source"};
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        JTable table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        JPanel loadingPanel = new JPanel();
        JLabel loadingLabel = new JLabel("Fetching jobs from LinkedIn via RapidAPI...");
        loadingPanel.add(loadingLabel);
        add(loadingPanel, BorderLayout.NORTH);

        // Fetch jobs in background
        SwingWorker<List<JobDescription>, Void> worker = new SwingWorker<>() {
            @Override
            protected List<JobDescription> doInBackground() {
                LinkedInJobsAPI api = new LinkedInJobsAPI();
                // Defaulting location to "Remote" for general matching
                return api.searchJobs(keyword, "Remote");
            }

            @Override
            protected void done() {
                try {
                    List<JobDescription> jobs = get();
                    if (jobs.isEmpty()) {
                        loadingLabel.setText("No jobs found for: " + keyword);
                    } else {
                        loadingLabel.setText("Found " + jobs.size() + " jobs.");
                        for (JobDescription jd : jobs) {
                            model.addRow(new Object[]{jd.getTitle(), jd.getCompany(), jd.getSource()});
                        }
                    }
                } catch (Exception e) {
                    loadingLabel.setText("Error fetching jobs.");
                    e.printStackTrace();
                }
            }
        };
        worker.execute();
    }
}
