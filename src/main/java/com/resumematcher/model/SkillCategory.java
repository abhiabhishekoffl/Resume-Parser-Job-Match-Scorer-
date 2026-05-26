package com.resumematcher.model;

public enum SkillCategory {
    PROGRAMMING,
    FRAMEWORK,
    DATABASE,
    CLOUD,
    MOBILE,
    DATA_ML,
    DEVOPS_TOOLS,
    SOFT_SKILL;

    public static SkillCategory fromString(String category) {
        if (category == null) return null;
        for (SkillCategory sc : SkillCategory.values()) {
            if (sc.name().equalsIgnoreCase(category.replace(" ", "_")) || 
                sc.name().equalsIgnoreCase(category.replace(" & ", "_"))) {
                return sc;
            }
        }
        
        // Custom fallbacks based on seed DB
        if (category.equalsIgnoreCase("Data & ML")) return DATA_ML;
        if (category.equalsIgnoreCase("DevOps & Tools")) return DEVOPS_TOOLS;
        if (category.equalsIgnoreCase("Soft Skill")) return SOFT_SKILL;
        if (category.equalsIgnoreCase("Programming Languages")) return PROGRAMMING;
        if (category.equalsIgnoreCase("Web Frameworks")) return FRAMEWORK;
        if (category.equalsIgnoreCase("Databases")) return DATABASE;
        if (category.equalsIgnoreCase("Cloud Platforms")) return CLOUD;
        
        return null;
    }
}
