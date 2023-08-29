package it.sepel.mailer.test.x;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestPec {

    private static final Logger log = LoggerFactory.getLogger(TestPec.class);

    private void go() throws Exception {
//        Properties p = new Properties();
//        p.setProperty("mail.smtp.host", "smtps.pec.aruba.it");
//        p.setProperty("mail.smtp.socketFactory.port", "465");
//        p.setProperty("mail.smtp.socketFactory.class","javax.net.ssl.SSLSocketFactory"); 
//        p.setProperty("mail.smtp.auth", "true");
//        p.setProperty("mail.smtp.port", "465");
//        SMTPAuthenticator auth = new SMTPAuthenticator();
//        auth.setUsername("statocivileitaliano@pec.it");
//        auth.setPassword("statocivile123");
        
//        Properties p = new Properties();
//        p.setProperty("mail.smtp.host", "smtp.pec.actalis.it");
//        p.setProperty("mail.smtp.socketFactory.port", "25");
//        p.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
//        p.setProperty("mail.smtp.auth", "true");
//        p.setProperty("mail.smtp.port", "25");
//        SMTPAuthenticator auth = new SMTPAuthenticator();
//        auth.setUsername("graduatoriamedici@postacert.regione.emilia-romagna.it");
//        auth.setPassword("cjvNDwK5");
//        Mailer.initSession(p, auth);
//        InternetAddress to = new InternetAddress("mario.lombardini@ingpec.eu");
//        InternetAddress from = new InternetAddress("graduatoriamedici@postacert.regione.emilia-romagna.it");
//        
////        Properties p = new Properties();
////        p.setProperty("mail.smtp.host", "statocivile.it");
////        p.setProperty("mail.smtp.port", "25");
////        Mailer.initSession(p, null);
////        InternetAddress to = new InternetAddress("mario@3wps.com");
////        InternetAddress from = new InternetAddress("newsletter@sepel.it");
//
//        log.debug("Sending mail...");
//        String subject = "Prova Pec JAVA";
//        String text = "Prova messaggio di pec con JAVA";
//        Mailer.sendPlainText(to, from, subject, text);
        log.debug("Mail sended!");


    }

    public static void main(String[] args) {
        TestPec t = new TestPec();
        try {
            t.go();
        } catch (Exception e) {
            log.error("Errore", e);
        }
    }
}
