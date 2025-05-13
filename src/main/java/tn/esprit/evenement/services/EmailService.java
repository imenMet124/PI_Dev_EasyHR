package tn.esprit.evenement.services;

import jakarta.mail.*;
import jakarta.mail.internet.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class EmailService {
    private String senderEmail;
    private String senderPassword;
    private String smtpHost;
    private String smtpPort;

    public EmailService() {
        Properties properties = new Properties();

        try (InputStream input = getClass().getClassLoader().getResourceAsStream("config.properties")) {
            if (input == null) {
                throw new IOException("❌ Erreur : Fichier config.properties introuvable !");
            }
            properties.load(input);

            senderEmail = properties.getProperty("mail.sender.email");
            senderPassword = properties.getProperty("mail.sender.password");
            smtpHost = properties.getProperty("mail.smtp.host");
            smtpPort = properties.getProperty("mail.smtp.port");
        } catch (IOException e) {
            System.out.println("❌ Erreur : Impossible de charger le fichier config.properties !");
            e.printStackTrace();
        }
    }

    public void envoyerEmail(String destinataire, String sujet, String contenu) {
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", smtpHost);
        properties.put("mail.smtp.port", smtpPort);

        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(senderEmail, senderPassword);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(senderEmail));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(destinataire));
            message.setSubject(sujet);
            message.setText(contenu);

            Transport.send(message);
            System.out.println("📩 E-mail envoyé avec succès à : " + destinataire);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
