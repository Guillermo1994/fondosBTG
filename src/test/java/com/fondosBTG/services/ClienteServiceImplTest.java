package com.fondosBTG.services;
import com.fondosBTG.exception.ResourceNotFoundException;
import com.fondosBTG.models.Cliente;
import com.fondosBTG.repositories.IClienteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClienteServiceImplTest {

    @Mock
    private IClienteRepository clienteRepository;

    @InjectMocks
    private ClienteServiceImpl clienteService;

    private Cliente cliente;

    @BeforeEach
    void setUp() {
        cliente = new Cliente(); // Asegúrate de inicializar el objeto cliente según tu modelo
        cliente.setId("1"); // Asegúrate de establecer un ID o cualquier otra propiedad necesaria
        // Inicializa otras propiedades si es necesario
    }

    @Test
    void obtenerClientPorId_devuelveClienteCuandoExiste() {
        // Arrange
        when(clienteRepository.findById(cliente.getId())).thenReturn(Optional.of(cliente));

        // Act
        Cliente resultado = clienteService.obtenerClientPorId(cliente.getId());

        // Assert
        assertEquals(cliente, resultado);
        verify(clienteRepository, times(1)).findById(cliente.getId());
    }

    @Test
    void obtenerClientPorId_lanzaExcepcionCuandoNoExiste() {
        // Arrange
        String idInvalido = "2"; // ID que no existe
        when(clienteRepository.findById(idInvalido)).thenReturn(Optional.empty());

        // Act y Assert
        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            clienteService.obtenerClientPorId(idInvalido);
        });

        assertEquals("Cliente con ID " + idInvalido + " no encontrado.", exception.getMessage());
        verify(clienteRepository, times(1)).findById(idInvalido);
    }

    @Test
    void guardarCliente_devuelveClienteGuardado() {
        // Arrange
        when(clienteRepository.save(cliente)).thenReturn(cliente);

        // Act
        Cliente resultado = clienteService.guardarCliente(cliente);

        // Assert
        assertEquals(cliente, resultado);
        verify(clienteRepository, times(1)).save(cliente);
    }
}
