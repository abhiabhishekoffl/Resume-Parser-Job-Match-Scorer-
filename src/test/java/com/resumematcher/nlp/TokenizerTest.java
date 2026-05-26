package com.resumematcher.nlp;

import org.junit.Test;
import java.util.List;
import static org.junit.Assert.*;

public class TokenizerTest {
    @Test
    public void testTokenize() {
        Tokenizer tokenizer = new Tokenizer();
        List<String> tokens = tokenizer.tokenize("Java, Spring Boot! 10+ years");
        assertTrue(tokens.contains("Java"));
        assertTrue(tokens.contains("Spring"));
        assertTrue(tokens.contains("Boot"));
        assertTrue(tokens.contains("10+"));
    }
    
    @Test
    public void testEmptyTokenize() {
        Tokenizer tokenizer = new Tokenizer();
        List<String> tokens = tokenizer.tokenize("");
        assertTrue(tokens.isEmpty());
    }
}
