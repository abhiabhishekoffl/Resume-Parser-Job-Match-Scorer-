package com.resumematcher.scorer;

import com.resumematcher.model.JobDescription;
import com.resumematcher.model.Resume;
import com.resumematcher.model.ScanResult;
import com.resumematcher.model.Skill;
import com.resumematcher.nlp.SkillExtractor;
import com.resumematcher.nlp.Tokenizer;
import com.resumematcher.nlp.StopWordFilter;

import java.util.*;
import java.util.stream.Collectors;
import java.time.Instant;

public class ScoringEngine {
    
    private SkillExtractor skillExtractor;
    private Tokenizer tokenizer;
    private StopWordFilter stopWordFilter;
    
    public ScoringEngine() {
        this.skillExtractor = new SkillExtractor();
        this.tokenizer = new Tokenizer();
        this.stopWordFilter = new StopWordFilter();
    }

    public ScanResult computeScore(Resume r, JobDescription jd) {
        ScanResult result = new ScanResult();
        
        // 1. Extract Skills
        List<Skill> resumeSkills = skillExtractor.extractSkills(r.getExtractedText());
        List<Skill> jdSkills = skillExtractor.extractSkills(jd.getDescriptionText());
        r.setSkills(resumeSkills.stream().map(Skill::getName).collect(Collectors.toList()));
        
        // 2. Compute Skills Match (Jaccard)
        Set<String> rSkillSet = resumeSkills.stream().map(s -> s.getName().toLowerCase()).collect(Collectors.toSet());
        Set<String> jdSkillSet = jdSkills.stream().map(s -> s.getName().toLowerCase()).collect(Collectors.toSet());
        
        CoverageSimilarity coverage = new CoverageSimilarity();
        double rawSkillsScore = coverage.compute(rSkillSet, jdSkillSet) * 100.0;
        
        // 3. Gap Analysis
        GapAnalyzer gapAnalyzer = new GapAnalyzer();
        List<Skill> missingSkills = gapAnalyzer.findMissingSkills(resumeSkills, jdSkills);
        List<Skill> matchedSkills = new ArrayList<>(jdSkills);
        matchedSkills.removeAll(missingSkills);
        
        // 4. Experience Match
        double requiredExp = extractRequiredExperience(jd.getDescriptionText());
        double expScore = 100.0;
        if (requiredExp > 0) {
            expScore = Math.min(100.0, (r.getExperienceYears() / requiredExp) * 100.0);
        }
        
        // 5. Education Match (Simplified mock check)
        double eduScore = 100.0; 
        
        // 6. Keyword Match (TF-IDF)
        TFIDFCalculator tfidf = new TFIDFCalculator();
        List<String> rTokens = stopWordFilter.filter(tokenizer.tokenize(r.getExtractedText().toLowerCase()));
        List<String> jdTokens = stopWordFilter.filter(tokenizer.tokenize(jd.getDescriptionText().toLowerCase()));
        
        List<List<String>> corpus = Arrays.asList(rTokens, jdTokens);
        Map<String, Double> jdKeywords = tfidf.computeTFIDF(jdTokens, corpus);
        
        double keywordScore = 0.0;
        double maxTfidfScore = jdKeywords.values().stream().mapToDouble(v -> v).sum();
        if (maxTfidfScore > 0) {
            double currentTfidfSum = 0;
            for (String t : rTokens) {
                if (jdKeywords.containsKey(t)) {
                    currentTfidfSum += jdKeywords.get(t);
                }
            }
            keywordScore = Math.min(100.0, (currentTfidfSum / maxTfidfScore) * 100.0);
        }
        
        // 7. Weighted Combine
        Map<String, Double> subScores = new HashMap<>();
        subScores.put("skills", rawSkillsScore);
        subScores.put("experience", expScore);
        subScores.put("education", eduScore);
        subScores.put("keywords", keywordScore);
        
        WeightedScorer scorer = new WeightedScorer();
        double finalScore = scorer.computeWeightedScore(subScores);
        
        // Populate Result
        result.setResumeId(r.getId());
        result.setJobId(jd.getId());
        result.setTotalScore(finalScore);
        result.setSkillsScore(rawSkillsScore);
        result.setExperienceScore(expScore);
        result.setEducationScore(eduScore);
        result.setKeywordScore(keywordScore);
        result.setMatchedSkills(matchedSkills);
        result.setMissingSkills(missingSkills);
        result.setRecommendations(gapAnalyzer.generateRecommendations(missingSkills));
        result.setScanDate(Instant.now().toString());

        return result;
    }
    
    private double extractRequiredExperience(String jdText) {
        return 2.0; 
    }
}
