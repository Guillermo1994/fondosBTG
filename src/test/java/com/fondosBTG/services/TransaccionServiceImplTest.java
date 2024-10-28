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
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        cliente = new Cliente("cliente123", "Cliente Prueba", 1000.0,
                "cliente@mail.com", "123456789");
        fondo = new Fondo("fondo123", "Fondo Prueba", 500.00, "FVC");
    }

    @Test
    void realizarApertura_exitoEmail() {
        when(clienteService.obtenerClientPorId("cliente123")).thenReturn(cliente);
        when(fondoService.obtenerFondoPorId("fondo123")).thenReturn(fondo);

        String resultado = transaccionService.realizarApertura("cliente123", "fondo123",
                500.0, "EMAIL");

        assertEquals("Transacción de apertura exitosa", resultado);
        verify(transaccionRepository, times(1)).save(any(Transaccion.class));
        verify(emailService, times(1)).enviarEmail(eq("cliente@mail.com"),
                anyString(), anyString());
    }

    @Test
    void realizarApertura_exitoSMS() {
        when(clienteService.obtenerClientPorId("cliente123")).thenReturn(cliente);
        when(fondoService.obtenerFondoPorId("fondo123")).thenReturn(fondo);

        String resultado = transaccionService.realizarApertura("cliente123", "fondo123",
                500.0, "SMS");

        assertEquals("Transacción de apertura exitosa", resultado);
        verify(transaccionRepository, times(1)).save(any(Transaccion.class));
        verify(smsService, times(1)).enviarSms(eq("+57123456789"), anyString());
    }

    @Test
    void realizarApertura_clienteNoEncontrado() {
        when(clienteService.obtenerClientPorId("cliente123")).thenReturn(null);

        assertThrows(ResourceNotFoundException.class, () ->
                transaccionService.realizarApertura("cliente123", "fondo123", 500.0,
                        "EMAIL"));
    }

    @Test
    void realizarApertura_fondoNoEncontrado() {
        when(clienteService.obtenerClientPorId("cliente123")).thenReturn(cliente);
        when(fondoService.obtenerFondoPorId("fondo123")).thenReturn(null);

        assertThrows(ResourceNotFoundException.class, () ->
                transaccionService.realizarApertura("cliente123", "fondo123", 500.0,
                        "EMAIL"));
    }

    @Test
    void realizarApertura_saldoInsuficiente() {
        cliente.setSaldoInicial(100.0); // Saldo insuficiente
        when(clienteService.obtenerClientPorId("cliente123")).thenReturn(cliente);
        when(fondoService.obtenerFondoPorId("fondo123")).thenReturn(fondo);

        assertThrows(OperationNotAllowedException.class, () ->
                transaccionService.realizarApertura("cliente123", "fondo123", 500.0,
                        "EMAIL"));
    }

    @Test
    void realizarApertura_montoMenorAlMinimo() {
        when(clienteService.obtenerClientPorId("cliente123")).thenReturn(cliente);
        when(fondoService.obtenerFondoPorId("fondo123")).thenReturn(fondo);

        assertThrows(OperationNotAllowedException.class, () ->
                transaccionService.realizarApertura("cliente123", "fondo123", 200.0,
                        "EMAIL"));
    }

    @Test
    void realizarApertura_canalNotificacionInvalido() {
        when(clienteService.obtenerClientPorId("cliente123")).thenReturn(cliente);
        when(fondoService.obtenerFondoPorId("fondo123")).thenReturn(fondo);

        assertThrows(IllegalArgumentException.class, () ->
                transaccionService.realizarApertura("cliente123", "fondo123", 500.0,
                        "INVALIDO"));
    }

    @Test
    void realizarCancelacion_clienteNoEncontrado() {
        when(clienteService.obtenerClientPorId("123")).thenReturn(null);

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            transaccionService.realizarCancelacion("123", "fondo1", "transaccion1");
        });

        assertEquals("Cliente con ID 123 no encontrado.", exception.getMessage());
    }

    @Test
    void realizarCancelacion_fondoNoEncontrado() {
        Cliente cliente = new Cliente();
        when(clienteService.obtenerClientPorId("123")).thenReturn(cliente);
        when(fondoService.obtenerFondoPorId("fondo1")).thenReturn(null);

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            transaccionService.realizarCancelacion("123", "fondo1", "transaccion1");
        });

        assertEquals("Fondo con ID fondo1 no encontrado.", exception.getMessage());
    }

    @Test
    void realizarCancelacion_transaccionNoEncontrada() {
        Cliente cliente = new Cliente();
        Fondo fondo = new Fondo();
        when(clienteService.obtenerClientPorId("123")).thenReturn(cliente);
        when(fondoService.obtenerFondoPorId("fondo1")).thenReturn(fondo);
        when(transaccionRepository.findById("transaccion1")).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            transaccionService.realizarCancelacion("123", "fondo1", "transaccion1");
        });

        assertEquals("Transacción con ID transaccion1 no encontrada.", exception.getMessage());
    }

    @Test
    void realizarCancelacion_transaccionYaCancelada() {
        Cliente cliente = new Cliente();
        Fondo fondo = new Fondo();
        Transaccion transaccion = new Transaccion();
        transaccion.setTipo("Cancelación");
        when(clienteService.obtenerClientPorId("123")).thenReturn(cliente);
        when(fondoService.obtenerFondoPorId("fondo1")).thenReturn(fondo);
        when(transaccionRepository.findById("transaccion1")).thenReturn(Optional.of(transaccion));

        OperationNotAllowedException exception = assertThrows(OperationNotAllowedException.class, () -> {
            transaccionService.realizarCancelacion("123", "fondo1", "transaccion1");
        });

        assertEquals("No se puede cancelar una transacción de tipo Cancelación.", exception.getMessage());
    }

    @Test
    void realizarCancelacion_transaccionNoCorresponde() {
        Cliente cliente = new Cliente();
        Fondo fondo = new Fondo();
        Transaccion transaccion = new Transaccion();
        transaccion.setTipo("Apertura");
        transaccion.setClienteId("otroCliente");
        when(clienteService.obtenerClientPorId("123")).thenReturn(cliente);
        when(fondoService.obtenerFondoPorId("fondo1")).thenReturn(fondo);
        when(transaccionRepository.findById("transaccion1")).thenReturn(Optional.of(transaccion));

        OperationNotAllowedException exception = assertThrows(OperationNotAllowedException.class, () -> {
            transaccionService.realizarCancelacion("123", "fondo1", "transaccion1");
        });

        assertEquals("La transacción no corresponde al cliente o fondo indicado.", exception.getMessage());
    }

    @Test
    void realizarCancelacion_exito() {
        // Preparación de datos simulados
        Cliente cliente = new Cliente();
        cliente.setId("123");
        cliente.setSaldoInicial(1000.0);

        Fondo fondo = new Fondo();
        fondo.setId("fondo1");

        Transaccion transaccion = new Transaccion();
        transaccion.setTipo("Apertura");
        transaccion.setMonto(200);
        transaccion.setClienteId("123");
        transaccion.setFondoId("fondo1"); // Establece fondoId para evitar el NullPointerException

        // Mock de comportamiento
        when(clienteService.obtenerClientPorId("123")).thenReturn(cliente);
        when(fondoService.obtenerFondoPorId("fondo1")).thenReturn(fondo);
        when(transaccionRepository.findById("transaccion1")).thenReturn(Optional.of(transaccion));

        // Ejecución del método de prueba
        String resultado = transaccionService.realizarCancelacion("123", "fondo1", "transaccion1");

        // Verificación de resultados
        assertEquals("Transacción de cancelación exitosa", resultado);
        assertEquals(1200, cliente.getSaldoInicial());
        verify(transaccionRepository).save(any(Transaccion.class));
        verify(clienteService).guardarCliente(cliente);
    }
}
