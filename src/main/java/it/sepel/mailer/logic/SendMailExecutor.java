package it.sepel.mailer.logic;

import it.sepel.mailer.domain.Attachment;
import java.util.List;
import javax.mail.internet.InternetAddress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class SendMailExecutor {

    private static final Logger log = LoggerFactory.getLogger(SendMailExecutor.class);

    @Autowired
    private Mailer mailer;

    private class SenderThread implements Runnable {

        private  Logger log = LoggerFactory.getLogger(SenderThread.class);

        private final String to;
        private final String from;
        private final String subject;
        private final String templateName;
        private final Object model;
        private final List<Attachment> attachments;

        public SenderThread(String to, String from, String subject, String templateName, Object model, List<Attachment> attachments) {
            this.to = to;
            this.from = from;
            this.subject = subject;
            this.templateName = templateName;
            this.model = model;
            this.attachments = attachments;
        }

        @Override
        public void run() {
            try {
                log.info("Sending mail...");
                mailer.sendMessageTemplateText(new InternetAddress(to), new InternetAddress(from), subject, templateName, model, attachments);
                log.info("Mail sended!");
            } catch (Exception e) {
                log.info("La mail per [" + to + "] con subject [" + subject + "] non è stata inviata");
                log.error("ERRORE", e);
            }
        }
    }

    public void sendMailMessage(String to, String from, String subject, String templateName, Object model, List<Attachment> attachments) throws Exception {
        log.info("Starting sender thread message to [" + to + "]...");
        Thread thread = new Thread(new SenderThread(to, from, subject, templateName, model, attachments));
        thread.start();
        log.info("Sender thread started!");
    }
}
