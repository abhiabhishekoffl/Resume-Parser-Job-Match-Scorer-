package com.resumematcher.scorer;

import java.util.HashSet;
import java.util.Set;

public class CoverageSimilarity {
    public double compute(Set<String> set1, Set<String> set2) {
        if (set1.isEmpty() && set2.isEmpty()) return 1.0;
        if (set2.isEmpty()) return 0.0; // Cannot match empty requirements

        Set<String> intersection = new HashSet<>(set1);
        intersection.retainAll(set2);

        return (double) intersection.size() / set2.size();
    }
}
