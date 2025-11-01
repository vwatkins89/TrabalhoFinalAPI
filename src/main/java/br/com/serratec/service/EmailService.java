package br.com.serratec.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    /**
     * Envia email simples (texto). Lança MailException em falha.
     */
    public void enviarEmailSimples(String to, String subject, String text, String from) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        if (from != null && !from.isBlank()) {
            message.setFrom(from);
        }
        message.setSubject(subject);
        message.setText(text);
        mailSender.send(message);
    }

    /**
     * Versão sem 'from' (usa configuração padrão em spring.mail.username).
     */
    public void enviarEmailSimples(String to, String subject, String text) {
        enviarEmailSimples(to, subject, text, null);
    }
}