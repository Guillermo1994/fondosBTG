package com.fondosBTG.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

/**
 * Servicio para el envío de correos electrónicos.
 *
 * @author Guillermo Rqamirez
 */
@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    /**
     * Envía un correo electrónico con el asunto y el mensaje especificados.
     *
     * @param to      La dirección de correo electrónico del destinatario.
     * @param subject El asunto del correo electrónico.
     * @param text    El contenido del mensaje del correo electrónico.
     */
    public void enviarEmail(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        mailSender.send(message);
    }
}
