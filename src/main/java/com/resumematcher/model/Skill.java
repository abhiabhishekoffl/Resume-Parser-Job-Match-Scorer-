package com.resumematcher.model;

import java.util.List;
import java.util.Objects;

public class Skill {
    private String name;
    private SkillCategory category;
    private List<String> aliases;
    private double weight;

    public Skill(String name, SkillCategory category, List<String> aliases, double weight) {
        this.name = name;
        this.category = category;
        this.aliases = aliases;
        this.weight = weight;
    }

    public String getName() {
        return name;
    }

    public SkillCategory getCategory() {
        return category;
    }

    public List<String> getAliases() {
        return aliases;
    }

    public double getWeight() {
        return weight;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Skill skill = (Skill) o;
        return name.equalsIgnoreCase(skill.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name.toLowerCase());
    }
    
    @Override
    public String toString() {
        return name;
    }
}
