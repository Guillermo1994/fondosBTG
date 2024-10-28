package com.fondosBTG.services;

import com.fondosBTG.exception.ResourceNotFoundException;
import com.fondosBTG.models.Fondo;
import com.fondosBTG.repositories.IFondoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FondoServiceImplTest {

    @Mock
    private IFondoRepository fondoRepository;

    @InjectMocks
    private FondoServiceImpl fondoService;

    private Fondo fondo;

    @BeforeEach
    void setUp() {
        fondo = new Fondo();
        fondo.setId("1");
        fondo.setNombre("Fondo Test");
    }

    @Test
    void testObtenerFondos() {
        List<Fondo> fondos = Arrays.asList(fondo);
        when(fondoRepository.findAll()).thenReturn(fondos);

        List<Fondo> result = fondoService.obtenerFondos();

        assertEquals(1, result.size());
        assertEquals("Fondo Test", result.get(0).getNombre());
        verify(fondoRepository, times(1)).findAll();
    }

    @Test
    void testObtenerFondoPorId_ExistentId() {
        when(fondoRepository.findById("1")).thenReturn(Optional.of(fondo));

        Fondo result = fondoService.obtenerFondoPorId("1");

        assertNotNull(result);
        assertEquals("Fondo Test", result.getNombre());
        verify(fondoRepository, times(1)).findById("1");
    }

    @Test
    void testObtenerFondoPorId_NonExistentId() {
        when(fondoRepository.findById("2")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> fondoService.obtenerFondoPorId("2"));
        verify(fondoRepository, times(1)).findById("2");
    }

    @Test
    void testGuardarFondo() {
        when(fondoRepository.save(fondo)).thenReturn(fondo);

        Fondo result = fondoService.guardarFondo(fondo);

        assertNotNull(result);
        assertEquals("Fondo Test", result.getNombre());
        verify(fondoRepository, times(1)).save(fondo);
    }
}