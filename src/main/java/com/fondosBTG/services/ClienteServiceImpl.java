package com.fondosBTG.services;

import com.fondosBTG.exception.ResourceNotFoundException;
import com.fondosBTG.models.Cliente;
import com.fondosBTG.repositories.IClienteRepository;
import com.fondosBTG.services.IServices.IClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Implementación del servicio para la gestión de clientes.
 *
 * @author Guillermo Ramirez
 */
@Service
public class ClienteServiceImpl implements IClienteService {

    @Autowired
    private IClienteRepository clienteRepository;

    /**
     * Obtiene un cliente por su ID. Busca un cliente en el repositorio usando su identificador. Si el cliente no se
     * encuentra, lanza una excepción ResourceNotFoundException.
     *
     * @param id El identificador único del cliente.
     * @return El cliente encontrado.
     * @throws ResourceNotFoundException Si no se encuentra un cliente con el ID proporcionado.
     */
    @Override
    public Cliente obtenerClientPorId(String id) {
        return clienteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente con ID " + id + " no encontrado."));
    }

    /**
     * Guarda un cliente en el repositorio.
     *
     * <p>Si el cliente proporcionado es válido, se almacena en el repositorio.</p>
     *
     * @param cliente El cliente a guardar.
     * @return El cliente que fue guardado en el repositorio.
     */
    @Override
    public Cliente guardarCliente(Cliente cliente) {
        return clienteRepository.save(cliente);
    }
}
