package com.resumematcher.scorer;

import org.junit.Test;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import static org.junit.Assert.*;

public class TFIDFCalculatorTest {
    @Test
    public void testTFIDF() {
        TFIDFCalculator calculator = new TFIDFCalculator();
        List<String> doc1 = Arrays.asList("Java", "Spring", "Boot");
        List<String> doc2 = Arrays.asList("Java", "Python", "Django");
        List<List<String>> corpus = Arrays.asList(doc1, doc2);
        
        Map<String, Double> tfidf = calculator.computeTFIDF(doc1, corpus);
        
        assertTrue(tfidf.containsKey("Java"));
        assertTrue(tfidf.containsKey("Spring"));
        
        // "Java" appears in both docs, so its IDF should be lower than "Spring"
        assertTrue(tfidf.get("Spring") > tfidf.get("Java"));
    }
}
