package com.resumematcher.parser;

import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.List;

import static org.junit.Assert.*;

public class PDFParserTest {

    private PDFParser parser;

    @Before
    public void setUp() {
        parser = new PDFParser();
    }

    @Test
    public void testInvalidPDF() {
        File fakeFile = new File("nonexistent.pdf");
        assertFalse(parser.isValidPDF(fakeFile));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testExtractTextThrowsExceptionOnInvalidFile() {
        File fakeFile = new File("nonexistent.pdf");
        parser.extractText(fakeFile);
    }
    
    @Test
    public void testExtractPagesReturnsEmptyOnInvalidFile() {
        File fakeFile = new File("nonexistent.pdf");
        List<String> pages = parser.extractPages(fakeFile);
        assertTrue(pages.isEmpty());
    }

    // TODO: Add tests for valid PDF, password-protected PDF, empty PDF, multi-column layout
    // using actual mock files placed in src/test/resources/
}
