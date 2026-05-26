package com.resumematcher.nlp;

import java.time.LocalDate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ExperienceCalculator {

    public int calculateYearsExperience(String text) {
        if (text == null || text.isEmpty()) return 0;
        
        // 1. Try to find explicit ranges: 2018 - 2022, 2018-Present, Jan 2020 - Mar 2023, etc.
        Pattern rangePattern = Pattern.compile("((?:jan|feb|mar|apr|may|jun|jul|aug|sep|oct|nov|dec)?[a-z]*\\s*(20\\d{2}))\\s*(?:-|to|–|—)\\s*((?:jan|feb|mar|apr|may|jun|jul|aug|sep|oct|nov|dec)?[a-z]*\\s*(20\\d{2})|present|current)", Pattern.CASE_INSENSITIVE);
        Matcher rangeMatcher = rangePattern.matcher(text);
        
        double totalYears = 0;
        boolean foundRange = false;
        
        while (rangeMatcher.find()) {
            foundRange = true;
            String startYearStr = rangeMatcher.group(2);
            String endYearStr = rangeMatcher.group(4);
            
            if (startYearStr != null) {
                int start = Integer.parseInt(startYearStr);
                int end;
                if (endYearStr != null) {
                    end = Integer.parseInt(endYearStr);
                } else {
                    end = LocalDate.now().getYear();
                }
                
                int diff = end - start;
                if (diff >= 0 && diff <= 40) {
                    totalYears += diff;
                }
            }
        }
        
        // 2. Fallback: If no ranges found, just look for any standalone 4-digit years between 1980 and current year + 1
        if (!foundRange) {
            Pattern yearPattern = Pattern.compile("\\b(19[8-9]\\d|20[0-5]\\d)\\b");
            Matcher yearMatcher = yearPattern.matcher(text);
            int minYear = Integer.MAX_VALUE;
            int maxYear = Integer.MIN_VALUE;
            int currentYear = LocalDate.now().getYear();
            
            while (yearMatcher.find()) {
                int year = Integer.parseInt(yearMatcher.group(1));
                if (year <= currentYear + 1) { // allow +1 for "expected graduation" logic
                    if (year < minYear) minYear = year;
                    if (year > maxYear) maxYear = year;
                }
            }
            
            if (minYear != Integer.MAX_VALUE && maxYear != Integer.MIN_VALUE) {
                int diff = maxYear - minYear;
                if (diff == 0) {
                    totalYears = 1; // At least 1 year if a single year is mentioned
                } else {
                    totalYears = diff;
                }
            }
        }
        
        return (int) Math.round(totalYears);
    }
}
