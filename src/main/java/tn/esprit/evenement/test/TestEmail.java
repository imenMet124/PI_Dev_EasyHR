package tn.esprit.evenement.test;


import tn.esprit.evenement.services.EmailService;

public class TestEmail {
    public static void main(String[] args) {
        EmailService emailService = new EmailService();
        emailService.envoyerEmail("methniimen@gmail.com", "Test Email", "Ceci est un test d'envoi d'e-mail.");
    }
}
