package project.email.config;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
public class EmailSenderConfig {


    @Value("${email.sender.host}")
    private String host;

    @Value("${email.sender.port}")
    private String port;

    @Value("${email.sender.username}")
    private String username;

    @Value("${email.sender.password}")
    private String password;

    @Value("${email.transport.protocol}")
    private String protocol;

    @Value("${email.smtp.auth}")
    private String auth;

    @Value("${email.smtp.starttls.enable}")
    private String ttlsEnable;

    @Value("${email.debug}")
    private String debug;

    @Bean
    public JavaMailSender getJavaMailSender (){
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(host);
        mailSender.setPort(Integer.valueOf(port));

        mailSender.setUsername(username);
        mailSender.setPassword(password);
        mailSender.setJavaMailProperties(getEmailSenderProperties());

        return mailSender;
    }

    private Properties getEmailSenderProperties (){
        Properties properties = new Properties();
        properties.put("mail.transport.protocol", protocol);
        properties.put("mail.smtp.auth",auth);
        properties.put("mail.stmp.starttls.enable", ttlsEnable);
        properties.put("mail.debug",debug);
        return properties;
    }

}
