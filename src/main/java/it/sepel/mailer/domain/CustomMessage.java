package it.sepel.mailer.domain;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.MimeMessage;

public class CustomMessage extends MimeMessage {
    
    public CustomMessage(Session session) {
        super(session);
    }

    @Override
    protected void updateMessageID() throws MessagingException {
        if (getHeader("Message-Id") == null) {
            super.updateMessageID();
        }
    }
    
}
