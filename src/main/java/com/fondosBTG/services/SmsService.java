package com.fondosBTG.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.PublishRequest;
import software.amazon.awssdk.services.sns.model.PublishResponse;

@Service

public class SmsService {

    private SnsClient snsClient;

    @Autowired
    public SmsService(SnsClient snsClient) {
        this.snsClient = snsClient;
    }

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
