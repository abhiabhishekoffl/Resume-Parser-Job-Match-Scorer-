package com.resumematcher.nlp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Tokenizer {

    public List<String> tokenize(String text) {
        if (text == null || text.trim().isEmpty()) {
            return new ArrayList<>();
        }
        
        // Simple tokenization: split by whitespace and punctuation
        // Keeps alphanumeric and some special chars common in tech (like C++, C#, .NET)
        String[] rawTokens = text.split("[\\s,;:\\(\\)\\[\\]\\{\\}]+");
        
        List<String> tokens = new ArrayList<>();
        for (String t : rawTokens) {
            String clean = t.trim().replaceAll("^[^a-zA-Z0-9+#]+|[^a-zA-Z0-9+#]+$", "");
            if (!clean.isEmpty()) {
                tokens.add(clean);
            }
        }
        return tokens;
    }

    public List<String> tokenizeNGrams(String text, int n) {
        List<String> words = tokenize(text);
        List<String> ngrams = new ArrayList<>();
        
        if (n <= 1) {
            return words;
        }

        for (int i = 0; i <= words.size() - n; i++) {
            StringBuilder sb = new StringBuilder();
            for (int j = 0; j < n; j++) {
                sb.append(words.get(i + j)).append(j < n - 1 ? " " : "");
            }
            ngrams.add(sb.toString());
        }
        
        return ngrams;
    }
}
