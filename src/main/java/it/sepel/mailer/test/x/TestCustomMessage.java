package it.sepel.mailer.test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestCustomMessage {

    private static final Logger log = LoggerFactory.getLogger(TestCustomMessage.class);

    private void go() throws Exception {
//        Properties p = new Properties();
//        p.setProperty("mail.smtp.host", "statocivile.it");
//        p.setProperty("mail.smtp.port", "25");
//        Mailer.initSession(p, null);
//        InternetAddress to = new InternetAddress("mariolomb70@gmail.com");
//        InternetAddress from = new InternetAddress("newsletter@sepel.it");
//
//        log.debug("Sending mail...");
//        String subject = "Prova Custom message ";
//        String text = "Prova messaggio con message id";
//        String messageId = "<" + UUID.randomUUID().toString() + "@" + "statocivile.it>";
//        Mailer.sendHtmlText(to, from, subject, text, text, null, null, messageId);
        log.debug("Mail sended!");
    }

    public static void main(String[] args) {
        TestCustomMessage t = new TestCustomMessage();
        try {
            t.go();
        } catch (Exception e) {
            log.error("Errore", e);
        }
    }
}
