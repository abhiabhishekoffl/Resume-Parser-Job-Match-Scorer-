package com.resumematcher.model;

import java.util.List;

public class ScanResult {
    private int id;
    private int resumeId;
    private int jobId;
    private double totalScore;
    private double skillsScore;
    private double experienceScore;
    private double educationScore;
    private double keywordScore;
    private List<Skill> matchedSkills;
    private List<Skill> missingSkills;
    private List<String> recommendations;
    private String scanDate;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getResumeId() { return resumeId; }
    public void setResumeId(int resumeId) { this.resumeId = resumeId; }

    public int getJobId() { return jobId; }
    public void setJobId(int jobId) { this.jobId = jobId; }

    public double getTotalScore() { return totalScore; }
    public void setTotalScore(double totalScore) { this.totalScore = totalScore; }

    public double getSkillsScore() { return skillsScore; }
    public void setSkillsScore(double skillsScore) { this.skillsScore = skillsScore; }

    public double getExperienceScore() { return experienceScore; }
    public void setExperienceScore(double experienceScore) { this.experienceScore = experienceScore; }

    public double getEducationScore() { return educationScore; }
    public void setEducationScore(double educationScore) { this.educationScore = educationScore; }

    public double getKeywordScore() { return keywordScore; }
    public void setKeywordScore(double keywordScore) { this.keywordScore = keywordScore; }

    public List<Skill> getMatchedSkills() { return matchedSkills; }
    public void setMatchedSkills(List<Skill> matchedSkills) { this.matchedSkills = matchedSkills; }

    public List<Skill> getMissingSkills() { return missingSkills; }
    public void setMissingSkills(List<Skill> missingSkills) { this.missingSkills = missingSkills; }

    public List<String> getRecommendations() { return recommendations; }
    public void setRecommendations(List<String> recommendations) { this.recommendations = recommendations; }

    public String getScanDate() { return scanDate; }
    public void setScanDate(String scanDate) { this.scanDate = scanDate; }
}
