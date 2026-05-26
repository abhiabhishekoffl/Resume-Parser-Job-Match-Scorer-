package com.resumematcher.nlp;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.resumematcher.db.DatabaseManager;
import com.resumematcher.model.Skill;
import com.resumematcher.model.SkillCategory;

import java.lang.reflect.Type;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class SkillExtractor {
    private List<Skill> dictionary;
    private Tokenizer tokenizer;
    private StopWordFilter stopWordFilter;

    public SkillExtractor() {
        dictionary = new ArrayList<>();
        tokenizer = new Tokenizer();
        stopWordFilter = new StopWordFilter();
        loadSkillsDictionary();
    }

    public void loadSkillsDictionary() {
        Gson gson = new Gson();
        Type listType = new TypeToken<List<String>>(){}.getType();
        
        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM skills_dict")) {
            
            while (rs.next()) {
                String name = rs.getString("skill_name");
                String catStr = rs.getString("category");
                String aliasesJson = rs.getString("aliases_json");
                double weight = rs.getDouble("weight");

                SkillCategory cat = SkillCategory.fromString(catStr);
                List<String> aliases = new ArrayList<>();
                if (aliasesJson != null && !aliasesJson.isEmpty()) {
                    aliases = gson.fromJson(aliasesJson, listType);
                }

                dictionary.add(new Skill(name, cat, aliases, weight));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Skill> extractSkills(String text) {
        List<Skill> foundSkills = new ArrayList<>();
        if (text == null || text.trim().isEmpty()) return foundSkills;

        String lowerText = text.toLowerCase();
        
        // We will do a combination of string matching and token matching
        // to support multi-word skills like "Spring Boot"
        
        for (Skill skill : dictionary) {
            boolean matched = false;
            
            // Check name
            if (matchesExact(lowerText, skill.getName().toLowerCase())) {
                matched = true;
            } else {
                // Check aliases
                for (String alias : skill.getAliases()) {
                    if (matchesExact(lowerText, alias.toLowerCase())) {
                        matched = true;
                        break;
                    }
                }
            }
            
            if (matched && !foundSkills.contains(skill)) {
                foundSkills.add(skill);
            }
        }

        return foundSkills;
    }
    
    private boolean matchesExact(String fullText, String keyword) {
        // Simple heuristic: check if keyword exists surrounded by non-alphanumeric chars or boundaries
        String regex = "(^|[^a-zA-Z0-9+#])" + java.util.regex.Pattern.quote(keyword) + "([^a-zA-Z0-9+#]|$)";
        return java.util.regex.Pattern.compile(regex).matcher(fullText).find();
    }

    public boolean matchesAlias(String token, Skill skill) {
        if (token.equalsIgnoreCase(skill.getName())) return true;
        for (String alias : skill.getAliases()) {
            if (token.equalsIgnoreCase(alias)) return true;
        }
        return false;
    }
}
