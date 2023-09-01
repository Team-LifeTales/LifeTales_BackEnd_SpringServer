package com.LifeTales.global.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
public class EmailConfig {

    @Value("${spring.mail.port}")
    private int port;
    @Value("${spring.mail.properties.socketFactory.port}")
    private int socketPort;
    @Value("${spring.mail.properties.auth}")
    private boolean auth;
    @Value("${spring.mail.properties.starttls.enable}")
    private boolean starttls;
    @Value("${spring.mail.properties.starttls.required}")
    private boolean starttlsRequired;
    @Value("${spring.mail.properties.socketFactory.fallback}")
    private boolean fallback;
    @Value("${spring.mail.username}")
    private String id;
    @Value("${spring.mail.password}")
    private String password;

    @Bean
    public JavaMailSender javaMailSender() {
        JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
        javaMailSender.setHost("smtp.gmail.com");
        javaMailSender.setUsername(id);
        javaMailSender.setPassword(password);
        javaMailSender.setPort(port);
        javaMailSender.setJavaMailProperties(getMailProperties());
        javaMailSender.setDefaultEncoding("UTF-8");
        return javaMailSender;
    }

    private Properties getMailProperties() {
        Properties properties = new Properties();
        properties.put("mail.smtp.socketFactory.port", socketPort);
        properties.put("mail.smtp.auth", auth);
        properties.put("mail.smtp.starttls.enable", starttls);
        properties.put("mail.smtp.starttls.required", starttlsRequired);
        properties.put("mail.smtp.socketFactory.fallback", fallback);
        properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        return properties;
    }
}
