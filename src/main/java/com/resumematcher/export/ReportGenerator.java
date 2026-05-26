package com.resumematcher.export;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.resumematcher.config.AppConfig;
import com.resumematcher.model.Resume;
import com.resumematcher.model.ScanResult;
import com.resumematcher.model.Skill;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Collectors;

public class ReportGenerator {

    private static final Font TITLE_FONT = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD);
    private static final Font HEADER_FONT = new Font(Font.FontFamily.HELVETICA, 14, Font.BOLD);
    private static final Font NORMAL_FONT = new Font(Font.FontFamily.HELVETICA, 12, Font.NORMAL);
    private static final Font BOLD_FONT = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);

    public File generatePDFReport(ScanResult result, Resume resume) {
        if (result == null || resume == null) {
            throw new IllegalArgumentException("ScanResult and Resume cannot be null");
        }

        try {
            Path reportsDir = Paths.get(System.getProperty("user.home"), ".resumematcher", "reports");
            File dir = reportsDir.toFile();
            if (!dir.exists()) {
                dir.mkdirs();
            }

            String filename = "MatchReport_" + resume.getFilename().replaceFirst("[.][^.]+$", "") + "_" + System.currentTimeMillis() + ".pdf";
            File pdfFile = new File(dir, filename);

            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream(pdfFile));
            document.open();

            // 1. Title
            Paragraph title = new Paragraph("Resume Match Report", TITLE_FONT);
            title.setAlignment(Element.ALIGN_CENTER);
            title.setSpacingAfter(20);
            document.add(title);

            // 2. Candidate Info
            document.add(new Paragraph("File Analyzed: " + resume.getFilename(), NORMAL_FONT));
            document.add(new Paragraph("Date: " + result.getScanDate(), NORMAL_FONT));
            document.add(new Paragraph("Overall Match Score: " + String.format("%.1f%%", result.getTotalScore()), HEADER_FONT));
            document.add(new Paragraph(" "));

            // 3. Sub-scores Table
            PdfPTable table = new PdfPTable(2);
            table.setWidthPercentage(100);
            table.setSpacingBefore(10);
            table.setSpacingAfter(20);

            addTableCell(table, "Category", true);
            addTableCell(table, "Score", true);

            addTableCell(table, "Skills Match", false);
            addTableCell(table, String.format("%.1f / 100", result.getSkillsScore()), false);

            addTableCell(table, "Experience Match", false);
            addTableCell(table, String.format("%.1f / 100", result.getExperienceScore()), false);

            addTableCell(table, "Education Match", false);
            addTableCell(table, String.format("%.1f / 100", result.getEducationScore()), false);

            addTableCell(table, "Keyword Match (TF-IDF)", false);
            addTableCell(table, String.format("%.1f / 100", result.getKeywordScore()), false);

            document.add(table);

            // 4. Missing Skills
            document.add(new Paragraph("Missing Skills (Gaps)", HEADER_FONT));
            String missing = result.getMissingSkills().stream()
                    .map(Skill::getName)
                    .collect(Collectors.joining(", "));
            
            Paragraph pMissing = new Paragraph(missing.isEmpty() ? "None! Perfect Match." : missing, NORMAL_FONT);
            pMissing.setSpacingAfter(15);
            document.add(pMissing);

            // 5. Recommendations
            document.add(new Paragraph("Recommendations", HEADER_FONT));
            com.itextpdf.text.List list = new com.itextpdf.text.List(com.itextpdf.text.List.UNORDERED);
            for (String rec : result.getRecommendations()) {
                list.add(new ListItem(rec, NORMAL_FONT));
            }
            document.add(list);

            document.close();
            return pdfFile;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private void addTableCell(PdfPTable table, String text, boolean isHeader) {
        PdfPCell cell = new PdfPCell(new Phrase(text, isHeader ? BOLD_FONT : NORMAL_FONT));
        if (isHeader) {
            cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
        }
        cell.setPadding(8);
        table.addCell(cell);
    }
}
