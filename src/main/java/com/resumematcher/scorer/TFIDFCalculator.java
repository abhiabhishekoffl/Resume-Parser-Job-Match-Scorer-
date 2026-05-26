package com.resumematcher.scorer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TFIDFCalculator {
    public Map<String, Double> computeTFIDF(List<String> tokens, List<List<String>> corpus) {
        Map<String, Double> tfidf = new HashMap<>();
        if (tokens == null || tokens.isEmpty() || corpus == null || corpus.isEmpty()) {
            return tfidf;
        }

        int totalDocs = corpus.size();
        
        // Count term frequency in the given token list
        Map<String, Integer> termCounts = new HashMap<>();
        for (String t : tokens) {
            termCounts.put(t, termCounts.getOrDefault(t, 0) + 1);
        }

        // Compute TF-IDF
        for (Map.Entry<String, Integer> entry : termCounts.entrySet()) {
            String term = entry.getKey();
            double tf = (double) entry.getValue() / tokens.size();
            
            // Document frequency
            int df = 0;
            for (List<String> doc : corpus) {
                if (doc.contains(term)) df++;
            }
            
            // If it's not in the corpus (shouldn't happen if the tokens are from the corpus, 
            // but for safety, we smooth it)
            df = Math.max(1, df);
            
            double idf = Math.log((double) totalDocs / df);
            tfidf.put(term, tf * idf);
        }

        return tfidf;
    }
}
