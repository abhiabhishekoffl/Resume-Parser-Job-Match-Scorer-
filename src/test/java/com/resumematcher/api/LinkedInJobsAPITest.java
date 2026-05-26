package com.resumematcher.api;

import com.resumematcher.model.JobDescription;
import com.resumematcher.config.AppConfig;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class LinkedInJobsAPITest {

    @Test
    public void testMockFallback() {
        AppConfig.loadConfig();
        // Without an API key set, it should fallback to mock data securely
        LinkedInJobsAPI api = new LinkedInJobsAPI();
        List<JobDescription> jobs = api.searchJobs("Java Developer", "New York");
        
        assertNotNull(jobs);
        assertFalse(jobs.isEmpty());
        assertEquals("Mock Data", jobs.get(0).getSource());
        assertTrue(jobs.get(0).getTitle().contains("Java Developer"));
    }
}
