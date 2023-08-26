package it.sepel.mailer;

import it.sepel.mailer.logic.SendMailExecutor;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class }) 
//@ComponentScan(basePackages = "it.sepel.mailer.test.*")
public class Application implements CommandLineRunner {
    
    @Autowired
    SendMailExecutor sme;
    
    private static final Logger log = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) {
        
        SpringApplication application = new SpringApplication(Application.class);
        application.setWebApplicationType(WebApplicationType.NONE);
        application.run(args);
        

        //SpringApplication.run(Application.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        log.info("Application is running");
        //mailer.test();
        
        String to = "mariolomb70@gmail.com";
        String from = "mario@sepel.it";
        String subject = "Prova messaggio";
        String text = "Test send thread";
        //mailer.sendPlainText(to, from, subject, text);
        Map model = new HashMap();
        model.put("nome", "Pippo");
        //mailer.sendMessageTemplate(to, from, subject, "test.ftl", model, null);
        
        //public void sendMailMessage(String to, String from, String subject, String templateName, Object model, List<Attachment> attachments)
        
        sme.sendMailMessage(to, from, subject, "test.ftl", model, null);
    }

}
