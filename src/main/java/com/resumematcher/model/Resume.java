package com.resumematcher.model;

import java.util.List;

public class Resume {
    private int id;
    private String filename;
    private String filepath;
    private String extractedText;
    private String uploadDate;
    private List<String> skills;
    private double experienceYears;
    private String highestDegree;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public String getFilename() { return filename; }
    public void setFilename(String filename) { this.filename = filename; }
    
    public String getFilepath() { return filepath; }
    public void setFilepath(String filepath) { this.filepath = filepath; }
    
    public String getExtractedText() { return extractedText; }
    public void setExtractedText(String extractedText) { this.extractedText = extractedText; }
    
    public String getUploadDate() { return uploadDate; }
    public void setUploadDate(String uploadDate) { this.uploadDate = uploadDate; }
    
    public List<String> getSkills() { return skills; }
    public void setSkills(List<String> skills) { this.skills = skills; }
    
    public double getExperienceYears() { return experienceYears; }
    public void setExperienceYears(double experienceYears) { this.experienceYears = experienceYears; }
    
    public String getHighestDegree() { return highestDegree; }
    public void setHighestDegree(String highestDegree) { this.highestDegree = highestDegree; }
}
