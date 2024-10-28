package com.fondosBTG.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.PublishRequest;
import software.amazon.awssdk.services.sns.model.PublishResponse;

/**
 * Servicio para el envío de mensajes SMS utilizando AWS SNS.
 *
 * @author Guillermo Ramirez
 */
@Service
public class SmsService {

    private SnsClient snsClient;

    /**
     * Constructor para inicializar el cliente de AWS SNS.
     *
     * @param snsClient El cliente SNS para enviar mensajes.
     */
    @Autowired
    public SmsService(SnsClient snsClient) {
        this.snsClient = snsClient;
    }

    /**
     * Envía un mensaje SMS al número de teléfono especificado.
     *
     * @param numeroTelefono El número de teléfono del destinatario en formato internacional.
     * @param mensaje        El contenido del mensaje a enviar.
     * @return El ID del mensaje enviado.
     * @throws IllegalArgumentException Si el número de teléfono es inválido, el mensaje está vacío o excede la longitud
     *                                  máxima.
     */
    public String enviarSms(String numeroTelefono, String mensaje) {
        // Validar número de teléfono
        if (numeroTelefono == null || !numeroTelefono.matches("\\+\\d+")) {
            throw new IllegalArgumentException("Número de teléfono inválido: " + numeroTelefono);
        }

        // Validar mensaje
        if (mensaje == null || mensaje.isEmpty()) {
            throw new IllegalArgumentException("El mensaje no puede estar vacío");
        }

        if (mensaje.length() > 2000) { // Supongamos que 2000 es la longitud máxima
            throw new IllegalArgumentException("El mensaje excede la longitud máxima permitida de 2000 caracteres");
        }

        // Crear y enviar la solicitud de publicación a SNS
        PublishRequest request = PublishRequest.builder()
                .message(mensaje)
                .phoneNumber(numeroTelefono)
                .build();

        PublishResponse result = snsClient.publish(request);
        return result.messageId();
    }
}
