package com.fondosBTG.services;

import com.fondosBTG.exception.OperationNotAllowedException;
import com.fondosBTG.exception.ResourceNotFoundException;
import com.fondosBTG.models.Cliente;
import com.fondosBTG.models.Fondo;
import com.fondosBTG.models.Transaccion;
import com.fondosBTG.repositories.ITransaccionRepository;
import com.fondosBTG.services.IServices.IClienteService;
import com.fondosBTG.services.IServices.IFondoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransaccionServiceImplTest {

    @Mock
    private ITransaccionRepository transaccionRepository;

    @Mock
    private IFondoService fondoService;

    @Mock
    private IClienteService clienteService;

    @Mock
    private EmailService emailService;

    @Mock
    private SmsService smsService;

    @InjectMocks
    private TransaccionServiceImpl transaccionService;

    private Cliente cliente;
    private Fondo fondo;
    private Transaccion transaccion;

    @BeforeEach
    void setUp() {
        cliente = new Cliente();
        cliente.setId("cliente123");
        cliente.setNombre("John Doe");
        cliente.setEmail("john@example.com");
        cliente.setTelefono("123456789");
        cliente.setSaldoInicial(1000.0);

        fondo = new Fondo();
        fondo.setId("fondo123");
        fondo.setNombre("Fondo ABC");
        fondo.setMontoMinimo(500.0);

        transaccion = Transaccion.builder()
                .id(UUID.randomUUID().toString())
                .tipo("Apertura")
                .clienteId(cliente.getId())
                .fondoId(fondo.getId())
                .fondoNombre(fondo.getNombre())
                .monto(600.0)
                .fecha(LocalDateTime.now())
                .build();

        // Usar lenient en stubs opcionales para evitar errores de Mockito innecesarios
        lenient().when(clienteService.obtenerClientPorId(anyString())).thenReturn(cliente);
        lenient().when(fondoService.obtenerFondoPorId(anyString())).thenReturn(fondo);
    }

    @Test
    void realizarApertura_clienteNoExiste_lanzaResourceNotFoundException() {
        when(clienteService.obtenerClientPorId(cliente.getId())).thenReturn(null);

        assertThrows(ResourceNotFoundException.class, () ->
                transaccionService.realizarApertura(cliente.getId(), fondo.getId(), 600.0, "EMAIL"));
    }

    @Test
    void realizarApertura_fondoNoExiste_lanzaResourceNotFoundException() {
        when(clienteService.obtenerClientPorId(cliente.getId())).thenReturn(cliente);
        when(fondoService.obtenerFondoPorId(fondo.getId())).thenReturn(null);

        assertThrows(ResourceNotFoundException.class, () ->
                transaccionService.realizarApertura(cliente.getId(), fondo.getId(), 600.0, "EMAIL"));
    }

    @Test
    void realizarApertura_saldoInsuficiente_lanzaOperationNotAllowedException() {
        cliente.setSaldoInicial(100.0);
        when(clienteService.obtenerClientPorId(cliente.getId())).thenReturn(cliente);
        when(fondoService.obtenerFondoPorId(fondo.getId())).thenReturn(fondo);

        assertThrows(OperationNotAllowedException.class, () ->
                transaccionService.realizarApertura(cliente.getId(), fondo.getId(), 600.0, "EMAIL"));
    }

    @Test
    void realizarApertura_montoMenorAlMinimo_lanzaOperationNotAllowedException() {
        when(clienteService.obtenerClientPorId(cliente.getId())).thenReturn(cliente);
        when(fondoService.obtenerFondoPorId(fondo.getId())).thenReturn(fondo);

        assertThrows(OperationNotAllowedException.class, () ->
                transaccionService.realizarApertura(cliente.getId(), fondo.getId(), 400.0, "EMAIL"));
    }

    @Test
    void realizarApertura_canalNotificacionInvalido_lanzaIllegalArgumentException() {
        when(clienteService.obtenerClientPorId(cliente.getId())).thenReturn(cliente);
        when(fondoService.obtenerFondoPorId(fondo.getId())).thenReturn(fondo);

        assertThrows(IllegalArgumentException.class, () ->
                transaccionService.realizarApertura(cliente.getId(), fondo.getId(), 600.0, "INVALID"));
    }

    @Test
    void realizarApertura_enviaNotificacionEmail() {
        when(clienteService.obtenerClientPorId(cliente.getId())).thenReturn(cliente);
        when(fondoService.obtenerFondoPorId(fondo.getId())).thenReturn(fondo);

        String resultado = transaccionService.realizarApertura(cliente.getId(), fondo.getId(), 600.0, "EMAIL");

        verify(emailService, times(1)).enviarEmail(eq(cliente.getEmail()), anyString(), anyString());
        assertEquals("Transacción de apertura exitosa", resultado);
    }

    @Test
    void realizarCancelacion_transaccionNoEncontrada_lanzaResourceNotFoundException() {
        when(transaccionRepository.findById(anyString())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () ->
                transaccionService.realizarCancelacion(cliente.getId(), fondo.getId(), "transaccionInvalida"));
    }

    @Test
    void realizarCancelacion_transaccionEsCancelacion_lanzaOperationNotAllowedException() {
        when(clienteService.obtenerClientPorId(cliente.getId())).thenReturn(cliente);
        when(fondoService.obtenerFondoPorId(fondo.getId())).thenReturn(fondo);

        transaccion.setTipo("Cancelación");
        when(transaccionRepository.findById(transaccion.getId())).thenReturn(Optional.of(transaccion));

        assertThrows(OperationNotAllowedException.class, () ->
                transaccionService.realizarCancelacion(cliente.getId(), fondo.getId(), transaccion.getId()));
    }



    void realizarCancelacion_exitosa() {
        when(clienteService.obtenerClientPorId(cliente.getId())).thenReturn(cliente);
        when(fondoService.obtenerFondoPorId(fondo.getId())).thenReturn(fondo);
        when(transaccionRepository.findById(transaccion.getId())).thenReturn(Optional.of(transaccion));

        String resultado = transaccionService.realizarCancelacion(cliente.getId(), fondo.getId(), transaccion.getId());

        assertEquals("Transacción de cancelación exitosa", resultado);
        verify(transaccionRepository, times(1)).save(any(Transaccion.class));
    }

    @Test
    void verHistorial_retornaListaTransacciones() {
        when(transaccionRepository.findByClienteId(cliente.getId())).thenReturn(List.of(transaccion));

        List<Transaccion> historial = transaccionService.verHistorial(cliente.getId());

        assertEquals(1, historial.size());
        assertEquals(transaccion.getId(), historial.get(0).getId());
    }
}
