package it.sepel.mailer.logic;

import freemarker.template.Configuration;
import freemarker.template.Template;
import it.sepel.mailer.domain.Attachment;
import it.sepel.mailer.domain.CustomMessage;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import javax.activation.*;
import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

public class Mailer {

    private static final Logger log = LoggerFactory.getLogger(Mailer.class);
    private static Session session;
    
    private String mailServerName;
    private String mailServerPort;
    private String mailServerUsername;
    private String mailServerPassword;
    
    public void test() {
        log.info("MAILER TEST!");
    }

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        Mailer.session = session;
    }

    public String getMailServerName() {
        return mailServerName;
    }

    public void setMailServerName(String mailServerName) {
        this.mailServerName = mailServerName;
    }

    public String getMailServerPort() {
        return mailServerPort;
    }

    public void setMailServerPort(String mailServerPort) {
        this.mailServerPort = mailServerPort;
    }

    public String getMailServerUsername() {
        return mailServerUsername;
    }

    public void setMailServerUsername(String mailServerUsername) {
        this.mailServerUsername = mailServerUsername;
    }

    public String getMailServerPassword() {
        return mailServerPassword;
    }

    public void setMailServerPassword(String mailServerPassword) {
        this.mailServerPassword = mailServerPassword;
    }

    public Configuration getFreemarkerConfig() {
        return freemarkerConfig;
    }

    public void setFreemarkerConfig(Configuration freemarkerConfig) {
        this.freemarkerConfig = freemarkerConfig;
    }
    
    @Autowired
    private Configuration freemarkerConfig;

    private void initSession() throws Exception {
        Properties p = new Properties();
        p.setProperty("mail.smtp.host", mailServerName);
        p.setProperty("mail.smtp.socketFactory.port", mailServerPort);
        p.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        p.setProperty("mail.smtp.auth", "true");
        p.setProperty("mail.smtp.port", mailServerPort);
        p.setProperty("mail.smtp.starttls.enable", "true");

        SMTPAuthenticator auth = new SMTPAuthenticator();
        auth.setUsername(mailServerUsername);
        auth.setPassword(mailServerPassword);

        //p.setProperty("transport", "smtp");
        //p.setProperty("mail.smtp.localhost", commonManager.loadConfigProperty(CommonConstants.CONFIG_PARAMETER_LOCALHOST_NAME));
        /*
            Properties p = new Properties();
            p.setProperty("mail.smtp.host", "smtps.pec.aruba.it");
            p.setProperty("mail.smtp.socketFactory.port", "465");
            p.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
            p.setProperty("mail.smtp.auth", "true");
            p.setProperty("mail.smtp.port", "465");
            SMTPAuthenticator auth = new SMTPAuthenticator();
            auth.setUsername("statocivileitaliano@pec.it");
            auth.setPassword("statocivile123");
            Mailer.initSession(p, auth);
         */
        session = Session.getInstance(p, auth);
    }

    public void closeSession() throws Exception {
        try {
            if (session != null) {
                Transport t = session.getTransport();
                if (t != null) {
                    t.close();
                }
            }
        } catch (MessagingException tt) {
            log.error("Errore", tt);
        }
    }

    //manda messaggi solo testo
    public void sendPlainText(InternetAddress to, InternetAddress from, String subject, String text) throws Exception {
        sendPlainText(to, from, subject, text, null, null);
    }

    private MimeMessage initMailMessage(InternetAddress to, InternetAddress from, String subject, Map<String, String> headers, String messageId) throws Exception {

        MimeMessage m;
        if (StringUtils.isNotEmpty(messageId)) {
            m = new CustomMessage(session);
            m.setHeader("Message-Id", messageId);
        } else {
            m = new MimeMessage(session);
        }
        m.addFrom(new Address[]{from});
        m.addRecipients(Message.RecipientType.TO, new Address[]{to});
        m.setSubject(subject);
        m.addHeader("X-Sender", from.getAddress());

        if (headers != null) {
            Set<String> kk = headers.keySet();
            for (String k : kk) {
                m.addHeader(k, headers.get(k));
            }
        }
        return m;
    }

    public void sendPlainText(InternetAddress to, InternetAddress from, String subject, String text, List<Attachment> attachments, Map headers) throws Exception {

        initSession();

        //creo messaggio
        MimeMessage m = initMailMessage(to, from, subject, headers, null);

        MimeBodyPart mbp = new MimeBodyPart();
        mbp.setContent(text, "text/plain; charset=ISO-8859-15; format=flowed");
        MimeMultipart mp = new MimeMultipart();
        mp.addBodyPart(mbp);

        //allegato
        if (attachments != null) {
            for (Attachment a : attachments) {
                log.debug("attachment name = [" + a.getName() + "]");
                log.debug("attachment type = [" + a.getType() + "]");
                log.debug("data = [" + Arrays.toString(a.getData()) + "]");
                mbp = new MimeBodyPart();
                DataSource source = new ByteArrayDataSource(a.getData(), a.getType());
                mbp.setDataHandler(new DataHandler(source));
                mbp.setFileName(a.getName());
                mp.addBodyPart(mbp);
            }
        }
        m.setContent(mp);

        //spedisco
        log.debug("Sending email message to [" + to + "]");
        Transport.send(m);
        closeSession();
        log.debug("Message sent!");
    }

    //server nel sendmail to all per gestire la sessione
    public String sendHtmlText(InternetAddress to, InternetAddress from, String subject, String html, String text, List<Attachment> attachments, Map headers, String messageId) throws Exception {

        initSession();

        //creo messaggio
        MimeMessage m = initMailMessage(to, from, subject, headers, messageId);

//        MimeMultipart mp = null;
//        if (attachments != null) {
//            mp = new MimeMultipart("mixed");
//        } else {
//            mp = new MimeMultipart("alternative");
//        }
        //main body
        Multipart mp = new MimeMultipart("related");

        //html e testo
        Multipart htmlAndTextMultipart = new MimeMultipart("alternative");

        MimeBodyPart mbp = new MimeBodyPart();
        if (text == null) {
            text = "";
        }
        mbp.setContent(text, "text/plain; charset=\"ISO-8859-1\"");
        htmlAndTextMultipart.addBodyPart(mbp);

        mbp = new MimeBodyPart();
        mbp.setContent(html, "text/html; charset=\"ISO-8859-1\"");
        htmlAndTextMultipart.addBodyPart(mbp);

        //metto html e text nel main
        MimeBodyPart htmlAndTextBodyPart = new MimeBodyPart();
        htmlAndTextBodyPart.setContent(htmlAndTextMultipart);
        mp.addBodyPart(htmlAndTextBodyPart);

        //parte con allegati
        if (attachments != null) {
            for (Attachment a : attachments) {
                MimeBodyPart filePart = new MimeBodyPart();
                log.debug("attachment name = [" + a.getName() + "]");
                log.debug("attachment type = [" + a.getType() + "]");
                log.debug("data = [" + Arrays.toString(a.getData()) + "]");
                DataSource source = new ByteArrayDataSource(a.getData(), a.getType());
                filePart.setDataHandler(new DataHandler(source));
                filePart.setFileName(a.getName());
                mp.addBodyPart(filePart);
            }
        }
        m.setContent(mp);

        //spedisco
        log.debug("Sending email message to [" + to + "]");
        Transport.send(m);
        String ret = m.getMessageID();
        log.debug("Message sended!");
        return ret;
    }

    //server nel sendmail to all per gestire la sessione
    public String sendHtmlText(InternetAddress to, InternetAddress from, String subject, String html, String text, List<Attachment> attachments, Map headers) throws Exception {
        return sendHtmlText(to, from, subject, html, text, attachments, headers, null);
    }

    //manda messaggio con template freemarker
    public void sendMessageTemplate(InternetAddress to, InternetAddress from, String subject, String templateName, Object templateInput, List<Attachment> attachment) throws Exception {
        /*
        String ENCODING = "ISO-8859-1";
        log.debug("Configuring freemarker...");
        Configuration cfg = new Configuration();
        cfg.setNumberFormat("0.######");
        cfg.setDirectoryForTemplateLoading(new File(commonManager.loadConfigProperty(CommonConstants.CONFIG_PARAMETER_TEMPLATE_DIRECTORY)));
        cfg.setObjectWrapper(new DefaultObjectWrapper());
        log.debug("Loading and processing template...");
        Template template = cfg.getTemplate(templateFile, ENCODING);
        template.setEncoding(ENCODING);

        ByteArrayOutputStream hout = new ByteArrayOutputStream();
        Writer writer = new OutputStreamWriter(hout, Charset.forName(ENCODING));
        Environment env = template.createProcessingEnvironment(templateInput, writer);
        env.setOutputEncoding(ENCODING);
        env.process();
        String html = new String(hout.toByteArray(), Charset.forName(ENCODING));
         */

        Template t = freemarkerConfig.getTemplate(templateName);
        String html = FreeMarkerTemplateUtils.processTemplateIntoString(t, templateInput);
        log.debug("Html message = [" + html + "]");

        sendHtmlText(to, from, subject, html, "", attachment, null);
    }

    public void sendMessageTemplateText(InternetAddress to, InternetAddress from, String subject, String templateName, Object templateInput, List<Attachment> attachment) throws Exception {
        /*
        String ENCODING = "ISO-8859-1";
        log.debug("Configuring freemarker...");
        Configuration cfg = new Configuration();
        cfg.setNumberFormat("0.######");
        cfg.setDirectoryForTemplateLoading(new File(commonManager.loadConfigProperty(CommonConstants.CONFIG_PARAMETER_TEMPLATE_DIRECTORY)));
        cfg.setObjectWrapper(new DefaultObjectWrapper());
        log.debug("Loading and processing template...");
        Template template = cfg.getTemplate(templateFile, ENCODING);
        template.setEncoding(ENCODING);
         */

        Template t = freemarkerConfig.getTemplate(templateName);
        String text = FreeMarkerTemplateUtils.processTemplateIntoString(t, templateInput);
        log.debug("Text message = [" + text + "]");

        /*
        ByteArrayOutputStream hout = new ByteArrayOutputStream();
        Writer writer = new OutputStreamWriter(hout, Charset.forName(ENCODING));
        Environment env = template.createProcessingEnvironment(templateInput, writer);
        env.setOutputEncoding(ENCODING);
        env.process();
        String text = new String(hout.toByteArray(), Charset.forName(ENCODING));
         */
        sendPlainText(to, from, subject, text, attachment, null);
    }
}
