package com.fondosBTG.controllers;

import com.fondosBTG.exception.ResourceNotFoundException;
import com.fondosBTG.models.Cliente;
import com.fondosBTG.services.IServices.IClienteService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ClienteControllerTest {

    @Mock
    private IClienteService clienteService;

    @InjectMocks
    private ClienteController clienteController;


    @Test
    public void testObtenerClienteExistente() {
        String clienteId = "123";
        Cliente cliente = new Cliente();
        cliente.setId(clienteId);

        when(clienteService.obtenerClientPorId(clienteId)).thenReturn(cliente);

        ResponseEntity<Cliente> response = clienteController.obtenerCliente(clienteId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(cliente, response.getBody());
        verify(clienteService, times(1)).obtenerClientPorId(clienteId);
    }

    @Test
    public void testObtenerClienteNoExistente() {
        String clienteId = "999";

        when(clienteService.obtenerClientPorId(clienteId)).thenThrow(new ResourceNotFoundException("Cliente no encontrado"));

        ResponseEntity<Cliente> response = clienteController.obtenerCliente(clienteId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(new Cliente(), response.getBody());
        verify(clienteService, times(1)).obtenerClientPorId(clienteId);
    }

    @Test
    public void testGuardarCliente() {
        Cliente cliente = new Cliente();
        cliente.setNombre("Juan Pérez");

        Cliente clienteGuardado = new Cliente();
        clienteGuardado.setId("123");
        clienteGuardado.setNombre("Juan Pérez");

        when(clienteService.guardarCliente(cliente)).thenReturn(clienteGuardado);

        ResponseEntity<Cliente> response = clienteController.guardaCliente(cliente);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(clienteGuardado, response.getBody());
        verify(clienteService, times(1)).guardarCliente(cliente);
    }
}