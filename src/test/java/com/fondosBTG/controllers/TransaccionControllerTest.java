package com.fondosBTG.controllers;

import com.fondosBTG.dto.AperturaPeticionDTO;
import com.fondosBTG.exception.OperationNotAllowedException;
import com.fondosBTG.exception.ResourceNotFoundException;
import com.fondosBTG.models.Transaccion;
import com.fondosBTG.services.IServices.ITransaccionService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TransaccionControllerTest {

    @Mock
    private ITransaccionService transaccionService;

    @InjectMocks
    private TransaccionController transaccionController;


    @Test
    void abrirFondo_exitoso_devuelveStatusCreated() {
        String clienteId = "123";
        String fondoId = "456";

        AperturaPeticionDTO aperturaRequest = new AperturaPeticionDTO();
        aperturaRequest.setMonto(1000.0);
        aperturaRequest.setCanalNotificacion("EMAIL");

        when(transaccionService.realizarApertura(clienteId, fondoId, aperturaRequest.getMonto(), aperturaRequest.getCanalNotificacion()))
                .thenReturn("Fondo abierto exitosamente");

        ResponseEntity<String> response = transaccionController.abrirFondo(clienteId, fondoId, aperturaRequest);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Fondo abierto exitosamente", response.getBody());
    }

    @Test
    void abrirFondo_fondoNoEncontrado_devuelveStatusNotFound() {
        String clienteId = "123";
        String fondoId = "456";

        AperturaPeticionDTO aperturaRequest = new AperturaPeticionDTO();
        aperturaRequest.setMonto(1000.0);
        aperturaRequest.setCanalNotificacion("EMAIL");

        when(transaccionService.realizarApertura(clienteId, fondoId, aperturaRequest.getMonto(), aperturaRequest.getCanalNotificacion()))
                .thenThrow(new ResourceNotFoundException("Fondo no encontrado"));

        ResponseEntity<String> response = transaccionController.abrirFondo(clienteId, fondoId, aperturaRequest);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Fondo no encontrado", response.getBody());
    }

    @Test
    void abrirFondo_operacionNoPermitida_devuelveStatusBadRequest() {
        String clienteId = "123";
        String fondoId = "456";

        AperturaPeticionDTO aperturaRequest = new AperturaPeticionDTO();
        aperturaRequest.setMonto(1000.0);
        aperturaRequest.setCanalNotificacion("EMAIL");

        when(transaccionService.realizarApertura(clienteId, fondoId, aperturaRequest.getMonto(), aperturaRequest.getCanalNotificacion()))
                .thenThrow(new OperationNotAllowedException("Operación no permitida"));

        ResponseEntity<String> response = transaccionController.abrirFondo(clienteId, fondoId, aperturaRequest);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Operación no permitida", response.getBody());
    }

    @Test
    void abrirFondo_errorInterno_devuelveStatusInternalServerError() {
        String clienteId = "123";
        String fondoId = "456";

        AperturaPeticionDTO aperturaRequest = new AperturaPeticionDTO();
        aperturaRequest.setMonto(1000.0);
        aperturaRequest.setCanalNotificacion("EMAIL");

        when(transaccionService.realizarApertura(clienteId, fondoId, aperturaRequest.getMonto(), aperturaRequest.getCanalNotificacion()))
                .thenThrow(new RuntimeException());

        ResponseEntity<String> response = transaccionController.abrirFondo(clienteId, fondoId, aperturaRequest);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Error al realizar la apertura del fondo.", response.getBody());
    }

    @Test
    void cancelarFondo_exitoso_devuelveStatusOk() {
        String clienteId = "123";
        String fondoId = "456";
        String transaccionId = "1";
        when(transaccionService.realizarCancelacion(clienteId, fondoId, transaccionId)).thenReturn("Fondo cancelado exitosamente");

        ResponseEntity<String> response = transaccionController.cancelarFondo(clienteId, fondoId, transaccionId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Fondo cancelado exitosamente", response.getBody());
    }

    @Test
    void cancelarFondo_fondoNoEncontrado_devuelveStatusNotFound() {
        String clienteId = "123";
        String fondoId = "456";
        String transaccionId = "1";
        when(transaccionService.realizarCancelacion(clienteId, fondoId, transaccionId))
                .thenThrow(new ResourceNotFoundException("Fondo no encontrado"));

        ResponseEntity<String> response = transaccionController.cancelarFondo(clienteId, fondoId, transaccionId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Fondo no encontrado", response.getBody());
    }

    @Test
    void cancelarFondo_operacionNoPermitida_devuelveStatusBadRequest() {
        String clienteId = "123";
        String fondoId = "456";
        String transaccionId = "1";
        when(transaccionService.realizarCancelacion(clienteId, fondoId, transaccionId))
                .thenThrow(new OperationNotAllowedException("Operación no permitida"));

        ResponseEntity<String> response = transaccionController.cancelarFondo(clienteId, fondoId, transaccionId);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Operación no permitida", response.getBody());
    }

    @Test
    void cancelarFondo_errorInterno_devuelveStatusInternalServerError() {
        String clienteId = "123";
        String fondoId = "456";
        String transaccionId = "1";
        when(transaccionService.realizarCancelacion(clienteId, fondoId, transaccionId)).thenThrow(new RuntimeException());

        ResponseEntity<String> response = transaccionController.cancelarFondo(clienteId, fondoId, transaccionId);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Error al realizar la cancelación del fondo.", response.getBody());
    }

    // Pruebas para el método verHistorial
    @Test
    void verHistorial_conHistorial_devuelveStatusOk() {
        String clienteId = "123";
        List<Transaccion> historial = List.of(new Transaccion());
        when(transaccionService.verHistorial(clienteId)).thenReturn(historial);

        ResponseEntity<List<Transaccion>> response = transaccionController.verHistorial(clienteId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(historial, response.getBody());
    }

    @Test
    void verHistorial_sinHistorial_devuelveStatusNotFound() {
        String clienteId = "123";
        when(transaccionService.verHistorial(clienteId)).thenReturn(Collections.emptyList());

        ResponseEntity<List<Transaccion>> response = transaccionController.verHistorial(clienteId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(Collections.emptyList(), response.getBody());
    }

    @Test
    void verHistorial_errorInterno_devuelveStatusInternalServerError() {
        String clienteId = "123";
        when(transaccionService.verHistorial(clienteId)).thenThrow(new RuntimeException());

        ResponseEntity<List<Transaccion>> response = transaccionController.verHistorial(clienteId);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals(null, response.getBody());
    }
}
