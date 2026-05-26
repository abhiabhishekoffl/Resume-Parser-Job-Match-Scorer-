package com.resumematcher.scorer;

import com.resumematcher.config.AppConfig;
import java.util.Map;

public class WeightedScorer {
    public double computeWeightedScore(Map<String, Double> scores) {
        double skillsWeight = Double.parseDouble(AppConfig.getProperty("scoring.weight.skills"));
        double expWeight = Double.parseDouble(AppConfig.getProperty("scoring.weight.experience"));
        double eduWeight = Double.parseDouble(AppConfig.getProperty("scoring.weight.education"));
        double keywordWeight = Double.parseDouble(AppConfig.getProperty("scoring.weight.keywords"));

        double skillsScore = scores.getOrDefault("skills", 0.0);
        double expScore = scores.getOrDefault("experience", 0.0);
        double eduScore = scores.getOrDefault("education", 0.0);
        double keywordScore = scores.getOrDefault("keywords", 0.0);

        // Sub-scores should ideally be bounded between 0 and 100
        double finalScore = (skillsScore * skillsWeight) +
                            (expScore * expWeight) +
                            (eduScore * eduWeight) +
                            (keywordScore * keywordWeight);
                            
        return Math.min(100.0, Math.max(0.0, finalScore));
    }
}
