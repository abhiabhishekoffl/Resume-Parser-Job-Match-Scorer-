package com.resumematcher.parser;

import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.List;

import static org.junit.Assert.*;

public class DOCXParserTest {

    private DOCXParser parser;

    @Before
    public void setUp() {
        parser = new DOCXParser();
    }

    @Test
    public void testInvalidDOCX() {
        File fakeFile = new File("nonexistent.docx");
        assertFalse(parser.isValidDOCX(fakeFile));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testExtractTextThrowsExceptionOnInvalidFile() {
        File fakeFile = new File("nonexistent.docx");
        parser.extractText(fakeFile);
    }

    @Test
    public void testExtractParagraphsReturnsEmptyOnInvalidFile() {
        File fakeFile = new File("nonexistent.docx");
        List<String> paras = parser.extractParagraphs(fakeFile);
        assertTrue(paras.isEmpty());
    }

    // TODO: Add tests for valid DOCX, DOCX with tables, DOCX with embedded images, empty DOCX
    // using actual mock files placed in src/test/resources/
}
