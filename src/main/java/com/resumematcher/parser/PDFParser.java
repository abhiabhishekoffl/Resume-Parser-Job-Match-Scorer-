package com.resumematcher.parser;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.text.PDFTextStripperByArea;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class PDFParser {

    public String extractText(File file) {
        if (!isValidPDF(file)) {
            throw new IllegalArgumentException("Invalid PDF file.");
        }
        
        try (PDDocument document = Loader.loadPDF(file)) {
            PDFTextStripper stripper = new PDFTextStripper();
            stripper.setSortByPosition(true);
            String rawText = stripper.getText(document);
            return TextCleaner.cleanResumeText(rawText);
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }

    public List<String> extractPages(File file) {
        List<String> pages = new ArrayList<>();
        if (!isValidPDF(file)) {
            return pages;
        }

        try (PDDocument document = Loader.loadPDF(file)) {
            PDFTextStripper stripper = new PDFTextStripper();
            stripper.setSortByPosition(true);
            int numPages = document.getNumberOfPages();
            
            for (int i = 1; i <= numPages; i++) {
                stripper.setStartPage(i);
                stripper.setEndPage(i);
                String pageText = stripper.getText(document);
                pages.add(TextCleaner.cleanResumeText(pageText));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return pages;
    }

    public boolean isValidPDF(File file) {
        if (file == null || !file.exists() || !file.isFile()) {
            return false;
        }
        
        try {
            byte[] header = new byte[4];
            java.io.FileInputStream fis = new java.io.FileInputStream(file);
            int bytesRead = fis.read(header);
            fis.close();
            
            if (bytesRead < 4) return false;
            
            String magic = new String(header);
            return magic.startsWith("%PDF");
        } catch (IOException e) {
            return false;
        }
    }

    public String extractWithLayout(File file) {
        if (!isValidPDF(file)) {
            return "";
        }

        try (PDDocument document = Loader.loadPDF(file)) {
            PDFTextStripperByArea stripper = new PDFTextStripperByArea();
            stripper.setSortByPosition(true);
            
            // For a robust layout extraction without knowing exact columns,
            // standard PDFTextStripper with sortByPosition=true is often sufficient.
            // But we will use the standard text stripper here as a fallback,
            // as defining regions without knowing the document structure is error-prone.
            PDFTextStripper generalStripper = new PDFTextStripper();
            generalStripper.setSortByPosition(true);
            return TextCleaner.cleanResumeText(generalStripper.getText(document));
            
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }
}
