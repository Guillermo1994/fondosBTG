package com.fondosBTG.controllers;

import com.fondosBTG.models.Fondo;
import com.fondosBTG.services.IServices.IFondoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class FondoControllerTest {

    @Mock
    private IFondoService fondoService;

    @InjectMocks
    private FondoController fondoController;

    @Test
    public void testObtenerFondos() {
        Fondo fondo1 = new Fondo();
        fondo1.setId("1");
        fondo1.setNombre("Fondo A");

        Fondo fondo2 = new Fondo();
        fondo2.setId("2");
        fondo2.setNombre("Fondo B");

        List<Fondo> fondos = Arrays.asList(fondo1, fondo2);

        when(fondoService.obtenerFondos()).thenReturn(fondos);

        ResponseEntity<List<Fondo>> response = fondoController.obtenerFondos();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(fondos, response.getBody());
        verify(fondoService, times(1)).obtenerFondos();
    }

    @Test
    public void testObtenerFondosError() {
        when(fondoService.obtenerFondos()).thenThrow(new RuntimeException("Error interno"));

        ResponseEntity<List<Fondo>> response = fondoController.obtenerFondos();

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals(null, response.getBody());
        verify(fondoService, times(1)).obtenerFondos();
    }

    @Test
    public void testGuardarFondo() {
        Fondo fondo = new Fondo();
        fondo.setNombre("Fondo C");

        Fondo fondoGuardado = new Fondo();
        fondoGuardado.setId("3");
        fondoGuardado.setNombre("Fondo C");

        when(fondoService.guardarFondo(fondo)).thenReturn(fondoGuardado);

        ResponseEntity<Fondo> response = fondoController.guardaFondo(fondo);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(fondoGuardado, response.getBody());
        verify(fondoService, times(1)).guardarFondo(fondo);
    }

    @Test
    public void testGuardarFondoError() {
        Fondo fondo = new Fondo();
        fondo.setNombre("Fondo D");

        when(fondoService.guardarFondo(fondo)).thenThrow(new RuntimeException("Error en el guardado"));

        ResponseEntity<Fondo> response = fondoController.guardaFondo(fondo);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(new Fondo(), response.getBody());
        verify(fondoService, times(1)).guardarFondo(fondo);
    }
}