package com.resumematcher.export;

import com.resumematcher.model.Resume;
import com.resumematcher.model.ScanResult;
import com.resumematcher.model.Skill;
import com.resumematcher.model.SkillCategory;

import org.junit.Test;
import java.io.File;
import java.util.Arrays;

import static org.junit.Assert.*;

public class ReportGeneratorTest {

    @Test
    public void testGeneratePDFReport() {
        ReportGenerator generator = new ReportGenerator();
        
        Resume resume = new Resume();
        resume.setFilename("TestCandidate_Resume.pdf");
        
        ScanResult result = new ScanResult();
        result.setTotalScore(85.5);
        result.setSkillsScore(90.0);
        result.setExperienceScore(80.0);
        result.setEducationScore(100.0);
        result.setKeywordScore(75.0);
        result.setMissingSkills(Arrays.asList(new Skill("AWS", SkillCategory.CLOUD, null, 1.0)));
        result.setRecommendations(Arrays.asList("Learn AWS basics."));
        result.setScanDate("2026-01-01T12:00:00Z");

        File pdf = generator.generatePDFReport(result, resume);
        
        assertNotNull(pdf);
        assertTrue(pdf.exists());
        assertTrue(pdf.getName().endsWith(".pdf"));
        
        // Clean up
        pdf.delete();
    }
}
