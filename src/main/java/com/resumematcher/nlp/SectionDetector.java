package com.resumematcher.nlp;

import java.util.HashMap;
import java.util.Map;

public class SectionDetector {
    
    // Strict match for standard uppercase section headers
    private static final String[] SECTION_HEADERS = {
        "EDUCATION", "EXPERIENCE", "WORK EXPERIENCE", "EMPLOYMENT", 
        "SKILLS", "TECHNICAL SKILLS", "PROJECTS", "SUMMARY", "PROFILE"
    };

    public Map<String, String> detectSections(String text) {
        Map<String, String> sections = new HashMap<>();
        if (text == null || text.isEmpty()) return sections;

        String[] lines = text.split("\n");
        String currentSection = "SUMMARY";
        StringBuilder currentContent = new StringBuilder();

        for (String line : lines) {
            String cleanLine = line.trim();
            boolean isHeader = false;
            
            // Heuristic for a header: all caps, short length, matches our known list
            if (cleanLine.length() > 0 && cleanLine.length() < 30 && cleanLine.toUpperCase().equals(cleanLine)) {
                for (String header : SECTION_HEADERS) {
                    if (cleanLine.equals(header) || cleanLine.matches("^" + header + "[\\s:]*$")) {
                        isHeader = true;
                        // Save previous section
                        if (currentContent.length() > 0) {
                            String existing = sections.getOrDefault(currentSection, "");
                            sections.put(currentSection, existing + "\n" + currentContent.toString().trim());
                        }
                        
                        // Map specific headers to general ones
                        if (header.contains("EXPERIENCE") || header.equals("EMPLOYMENT")) {
                            currentSection = "EXPERIENCE";
                        } else if (header.contains("SKILL")) {
                            currentSection = "SKILLS";
                        } else {
                            currentSection = header;
                        }
                        currentContent = new StringBuilder();
                        break;
                    }
                }
            }
            
            if (!isHeader) {
                currentContent.append(line).append("\n");
            }
        }
        
        // Save last section
        if (currentContent.length() > 0) {
            String existing = sections.getOrDefault(currentSection, "");
            sections.put(currentSection, existing + "\n" + currentContent.toString().trim());
        }

        return sections;
    }
}
