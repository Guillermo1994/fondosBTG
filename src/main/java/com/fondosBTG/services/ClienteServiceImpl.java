package com.fondosBTG.services;

import com.fondosBTG.exception.ResourceNotFoundException;
import com.fondosBTG.models.Cliente;
import com.fondosBTG.repositories.IClienteRepository;
import com.fondosBTG.services.IServices.IClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ClienteServiceImpl implements IClienteService {
    @Autowired
    private IClienteRepository clienteRepository;

    @Override
    public Cliente obtenerClientPorId(String id) {
        // Si no se encuentra el cliente, se lanza una excepción personalizada
        return clienteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente con ID " + id + " no encontrado."));
    }

    @Override
    public Cliente guardarCliente(Cliente cliente) {
        return clienteRepository.save(cliente);
    }
}
