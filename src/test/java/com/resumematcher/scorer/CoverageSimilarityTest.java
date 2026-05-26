package com.resumematcher.scorer;

import org.junit.Test;
import java.util.HashSet;
import java.util.Set;
import static org.junit.Assert.*;

public class CoverageSimilarityTest {

    @Test
    public void testComputeCoverage() {
        CoverageSimilarity sim = new CoverageSimilarity();
        Set<String> set1 = new HashSet<>();
        set1.add("Java");
        set1.add("Python");
        
        Set<String> set2 = new HashSet<>();
        set2.add("Java");
        set2.add("C++");
        
        double score = sim.compute(set1, set2);
        // Intersection = 1 (Java)
        // set2 size = 2 (Java, C++)
        // 1 / 2 = 0.5
        assertEquals(0.5, score, 0.001);
    }
}
