package com.fondosBTG.services;

import com.fondosBTG.exception.ResourceNotFoundException;
import com.fondosBTG.models.Cliente;
import com.fondosBTG.repositories.IClienteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ClienteServiceImplTest {

    @Mock
    private IClienteRepository clienteRepository;

    @InjectMocks
    private ClienteServiceImpl clienteService;

    private Cliente cliente;

    @BeforeEach
    void setUp() {
        cliente = new Cliente();
        cliente.setId("1");
    }

    @Test
    void obtenerClientPorId_devuelveClienteCuandoExiste() {
        when(clienteRepository.findById(cliente.getId())).thenReturn(Optional.of(cliente));

        Cliente resultado = clienteService.obtenerClientPorId(cliente.getId());

        assertEquals(cliente, resultado);
        verify(clienteRepository, times(1)).findById(cliente.getId());
    }

    @Test
    void obtenerClientPorId_lanzaExcepcionCuandoNoExiste() {
        String idInvalido = "2";
        when(clienteRepository.findById(idInvalido)).thenReturn(Optional.empty());

        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            clienteService.obtenerClientPorId(idInvalido);
        });

        assertEquals("Cliente con ID " + idInvalido + " no encontrado.", exception.getMessage());
        verify(clienteRepository, times(1)).findById(idInvalido);
    }

    @Test
    void guardarCliente_devuelveClienteGuardado() {
        when(clienteRepository.save(cliente)).thenReturn(cliente);

        Cliente resultado = clienteService.guardarCliente(cliente);

        assertEquals(cliente, resultado);
        verify(clienteRepository, times(1)).save(cliente);
    }
}
