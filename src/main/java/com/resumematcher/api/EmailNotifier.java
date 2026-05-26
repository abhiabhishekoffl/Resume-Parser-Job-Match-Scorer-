package com.resumematcher.api;

import com.resumematcher.config.AppConfig;
import com.resumematcher.model.ScanResult;

import jakarta.mail.*;
import jakarta.mail.internet.*;
import java.io.File;
import java.util.Properties;

public class EmailNotifier {

    public boolean sendResultEmail(String toEmail, ScanResult result, File attachment) {
        // Fallback defaults for missing config
        String host = AppConfig.getProperty("smtp.host", "smtp.gmail.com");
        String port = AppConfig.getProperty("smtp.port", "587");
        final String username = AppConfig.getProperty("smtp.username", "test@example.com");
        final String password = AppConfig.getProperty("smtp.password", "secret");

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", port);

        // For local development, if username is a stub or empty, don't actually try to send.
        if (username == null || username.isEmpty() || username.equals("test@example.com")) {
            System.out.println("Warning: SMTP credentials not configured in AppConfig. Mocking email send.");
            System.out.println("Mock Email sent to: " + toEmail + " with attachment: " + (attachment != null ? attachment.getName() : "None"));
            return true;
        }

        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            message.setSubject("Resume Match Report");

            BodyPart messageBodyPart = new MimeBodyPart();
            String content = "Hello,\n\nPlease find attached the Resume Match Report.\n\n"
                    + "Overall Score: " + String.format("%.1f%%", result.getTotalScore()) + "\n\n"
                    + "Regards,\nResume Matcher System";
            messageBodyPart.setText(content);

            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(messageBodyPart);

            if (attachment != null && attachment.exists()) {
                MimeBodyPart attachmentPart = new MimeBodyPart();
                attachmentPart.attachFile(attachment);
                multipart.addBodyPart(attachmentPart);
            }

            message.setContent(multipart);
            Transport.send(message);
            
            System.out.println("Email sent successfully.");
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
