package com.resumematcher.parser;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DOCXParser {

    public String extractText(File file) {
        if (!isValidDOCX(file)) {
            throw new IllegalArgumentException("Invalid DOCX file.");
        }

        StringBuilder sb = new StringBuilder();
        try (FileInputStream fis = new FileInputStream(file);
             XWPFDocument document = new XWPFDocument(fis)) {
            
            // Extract paragraphs
            for (XWPFParagraph para : document.getParagraphs()) {
                sb.append(para.getText()).append("\n");
            }
            
            // Extract tables
            sb.append("\n").append(extractTableText(document));
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return TextCleaner.cleanResumeText(sb.toString());
    }

    public List<String> extractParagraphs(File file) {
        List<String> paragraphs = new ArrayList<>();
        if (!isValidDOCX(file)) {
            return paragraphs;
        }

        try (FileInputStream fis = new FileInputStream(file);
             XWPFDocument document = new XWPFDocument(fis)) {
            
            for (XWPFParagraph para : document.getParagraphs()) {
                String text = TextCleaner.cleanResumeText(para.getText());
                if (!text.isEmpty()) {
                    paragraphs.add(text);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return paragraphs;
    }

    public String extractTableText(File file) {
        if (!isValidDOCX(file)) {
            return "";
        }
        
        try (FileInputStream fis = new FileInputStream(file);
             XWPFDocument document = new XWPFDocument(fis)) {
            return extractTableText(document);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
    
    private String extractTableText(XWPFDocument document) {
        StringBuilder sb = new StringBuilder();
        for (XWPFTable table : document.getTables()) {
            for (XWPFTableRow row : table.getRows()) {
                for (XWPFTableCell cell : row.getTableCells()) {
                    sb.append(cell.getText()).append(" | ");
                }
                sb.append("\n");
            }
            sb.append("\n");
        }
        return TextCleaner.cleanResumeText(sb.toString());
    }

    public boolean isValidDOCX(File file) {
        if (file == null || !file.exists() || !file.isFile()) {
            return false;
        }
        
        try (FileInputStream fis = new FileInputStream(file)) {
            byte[] header = new byte[4];
            int bytesRead = fis.read(header);
            
            if (bytesRead < 4) return false;
            
            // Magic bytes for ZIP files (DOCX is a zip archive) - PK\u0003\u0004
            return header[0] == 0x50 && header[1] == 0x4B && header[2] == 0x03 && header[3] == 0x04;
        } catch (IOException e) {
            return false;
        }
    }
}
