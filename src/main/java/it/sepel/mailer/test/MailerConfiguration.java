package it.sepel.mailer.test;

import it.sepel.mailer.logic.Mailer;
import it.sepel.mailer.logic.SendMailExecutor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MailerConfiguration {

    @Bean
    public Mailer mailer() {
        Mailer ret = new Mailer();
        ret.setMailServerName("mail.sepel.it");
        ret.setMailServerPort("587");
        ret.setMailServerUsername("mario@sepel.it");
        ret.setMailServerPassword("4kkd&c#@.M");
        return ret;
    }
    
    @Bean
    public SendMailExecutor sendMailExecutor() {
        SendMailExecutor ret = new SendMailExecutor();
        return ret;
    }
}
