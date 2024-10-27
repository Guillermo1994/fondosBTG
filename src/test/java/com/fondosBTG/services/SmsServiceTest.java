package com.fondosBTG.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.PublishRequest;
import software.amazon.awssdk.services.sns.model.PublishResponse;
import software.amazon.awssdk.services.sns.model.SnsException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
@ExtendWith(MockitoExtension.class)
class SmsServiceTest {

    @Mock
    private SnsClient snsClient;

    @InjectMocks
    private SmsService smsService;

    @Test
    void enviarSms_Exito_DeberiaRetornarMessageId() {
        // Configurar el mock para la respuesta exitosa
        PublishResponse response = PublishResponse.builder().messageId("12345").build();
        when(snsClient.publish(any(PublishRequest.class))).thenReturn(response);

        // Ejecutar el método y verificar el resultado
        String result = smsService.enviarSms("+123456789", "Mensaje de prueba");
        assertEquals("12345", result);
    }

    @Test
    void enviarSms_Fallo_DeberiaLanzarSnsException() {
        // Configurar el mock para lanzar una excepción
        when(snsClient.publish(any(PublishRequest.class))).thenThrow(SnsException.class);

        // Ejecutar el método y verificar que lanza la excepción esperada
        assertThrows(SnsException.class, () -> smsService.enviarSms("+123456789", "Mensaje de prueba"));
    }

    @Test
    void enviarSms_NumeroTelefonoInvalido_DeberiaLanzarIllegalArgumentException() {
        // Ejecutar el método con un número de teléfono inválido y verificar la excepción
        String numeroInvalido = "123ABC";
        IllegalArgumentException thrown = assertThrows(
                IllegalArgumentException.class,
                () -> smsService.enviarSms(numeroInvalido, "Mensaje de prueba")
        );
        assertEquals("Número de teléfono inválido: " + numeroInvalido, thrown.getMessage());
    }

    @Test
    void enviarSms_MensajeVacio_DeberiaLanzarIllegalArgumentException() {
        // Ejecutar el método con un mensaje vacío y verificar la excepción
        String mensajeVacio = "";
        IllegalArgumentException thrown = assertThrows(
                IllegalArgumentException.class,
                () -> smsService.enviarSms("+123456789", mensajeVacio)
        );
        assertEquals("El mensaje no puede estar vacío", thrown.getMessage());
    }

    @Test
    void enviarSms_MensajeExcedeMaximoLongitud_DeberiaLanzarIllegalArgumentException() {
        // Ejecutar el método con un mensaje que excede la longitud máxima y verificar la excepción
        String mensajeLargo = "a".repeat(2001);  // Suponiendo que el límite es 2000 caracteres
        IllegalArgumentException thrown = assertThrows(
                IllegalArgumentException.class,
                () -> smsService.enviarSms("+123456789", mensajeLargo)
        );
        assertEquals("El mensaje excede la longitud máxima permitida de 2000 caracteres", thrown.getMessage());
    }
}
