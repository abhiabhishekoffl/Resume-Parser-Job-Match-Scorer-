package com.resumematcher.parser;

public class TextCleaner {

    public static String cleanResumeText(String raw) {
        if (raw == null) return "";
        
        String cleaned = removeBulletSymbols(raw);
        cleaned = normalizeWhitespace(cleaned);
        
        return cleaned.trim();
    }

    public static String normalizeWhitespace(String text) {
        if (text == null) return "";
        
        // Replace multiple spaces/tabs with a single space
        String normalized = text.replaceAll("[ \t\u00A0]+", " ");
        // Ensure newlines are consistent and collapse multiple newlines to max two
        normalized = normalized.replaceAll("\r\n", "\n").replaceAll("\r", "\n");
        normalized = normalized.replaceAll("\n{3,}", "\n\n");
        
        return normalized;
    }

    public static String removeBulletSymbols(String text) {
        if (text == null) return "";
        
        // Matches common bullet points and arrows (unicode)
        String bulletRegex = "[\u2022\u25E6\u25A0\u2023\u25B8\u27A4\u27A2\u27BD\u27A1\u2794\u2192\u00B7\u25CB\u2713\u2714\u2013\u2014]";
        return text.replaceAll(bulletRegex, "");
    }
}
