package com.resumematcher.nlp;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class StopWordFilter {
    private Set<String> stopWords;

    public StopWordFilter() {
        stopWords = new HashSet<>();
        loadStopWords();
    }

    public void loadStopWords() {
        try (InputStream is = getClass().getResourceAsStream("/nlp/stopwords.txt")) {
            if (is != null) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                stopWords = reader.lines()
                        .map(String::trim)
                        .map(String::toLowerCase)
                        .collect(Collectors.toSet());
            } else {
                System.err.println("Could not find stopwords.txt");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<String> filter(List<String> tokens) {
        List<String> filtered = new ArrayList<>();
        if (tokens == null) return filtered;
        
        for (String token : tokens) {
            if (!stopWords.contains(token.toLowerCase())) {
                filtered.add(token);
            }
        }
        return filtered;
    }
}
