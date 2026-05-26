package com.resumematcher.nlp;

public class EducationExtractor {

    public String extractHighestDegree(String text) {
        if (text == null || text.isEmpty()) return "Unknown";
        
        String lowerText = text.toLowerCase();
        
        // Hierarchy check
        if (lowerText.contains("phd") || lowerText.contains("ph.d") || lowerText.contains("doctorate")) {
            return "PhD";
        }
        
        if (lowerText.contains("master") || lowerText.contains("m.tech") || lowerText.contains("msc") || 
            lowerText.contains("m.s") || lowerText.contains("mba") || lowerText.contains("m.a")) {
            return "Master's Degree";
        }
        
        if (lowerText.contains("bachelor") || lowerText.contains("b.tech") || lowerText.contains("bsc") || 
            lowerText.contains("b.s") || lowerText.contains("b.a") || lowerText.contains("b.e")) {
            return "Bachelor's Degree";
        }
        
        if (lowerText.contains("associate")) {
            return "Associate Degree";
        }
        
        if (lowerText.contains("diploma")) {
            return "Diploma";
        }
        
        if (lowerText.contains("high school")) {
            return "High School";
        }

        return "Unknown";
    }
}
