package com.resumematcher.scorer;

import com.resumematcher.model.Skill;
import java.util.ArrayList;
import java.util.List;

public class GapAnalyzer {
    public List<Skill> findMissingSkills(List<Skill> resumeSkills, List<Skill> jdSkills) {
        List<Skill> gaps = new ArrayList<>();
        if (jdSkills == null) return gaps;
        
        List<String> resumeSkillNames = new ArrayList<>();
        if (resumeSkills != null) {
            for (Skill s : resumeSkills) resumeSkillNames.add(s.getName().toLowerCase());
        }
        
        for (Skill reqSkill : jdSkills) {
            if (!resumeSkillNames.contains(reqSkill.getName().toLowerCase())) {
                gaps.add(reqSkill);
            }
        }
        return gaps;
    }

    public List<String> generateRecommendations(List<Skill> gaps) {
        List<String> recommendations = new ArrayList<>();
        if (gaps == null || gaps.isEmpty()) {
            recommendations.add("Great match! Your resume covers all required skills.");
            return recommendations;
        }

        for (Skill missing : gaps) {
            switch (missing.getCategory()) {
                case PROGRAMMING:
                    recommendations.add("Consider taking a short course or building a project using " + missing.getName() + ".");
                    break;
                case FRAMEWORK:
                    recommendations.add("Familiarize yourself with the " + missing.getName() + " framework and add it to your portfolio.");
                    break;
                case DATABASE:
                    recommendations.add("Learn basic CRUD operations and querying in " + missing.getName() + ".");
                    break;
                case CLOUD:
                    recommendations.add("Look into getting an entry-level certification for " + missing.getName() + ".");
                    break;
                case SOFT_SKILL:
                    recommendations.add("Highlight any experiences where you demonstrated " + missing.getName() + ".");
                    break;
                default:
                    recommendations.add("Gain some exposure to " + missing.getName() + ".");
                    break;
            }
        }
        return recommendations;
    }
}
