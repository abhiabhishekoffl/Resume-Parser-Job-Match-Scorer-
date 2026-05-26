package com.resumematcher.api;

import com.resumematcher.model.ScanResult;
import com.resumematcher.config.AppConfig;
import org.junit.Test;

import static org.junit.Assert.*;

public class EmailNotifierTest {

    @Test
    public void testMockEmailNotifier() {
        AppConfig.loadConfig();
        EmailNotifier notifier = new EmailNotifier();
        ScanResult result = new ScanResult();
        result.setTotalScore(85.0);
        
        // As long as smtp.username is "test@example.com" in AppConfig (the default),
        // this will return true via the mock path without throwing exceptions.
        boolean success = notifier.sendResultEmail("user@example.com", result, null);
        assertTrue(success);
    }
}
