package com.fondosBTG.controllers;

import com.fondosBTG.exception.ResourceNotFoundException;
import com.fondosBTG.models.Cliente;
import com.fondosBTG.services.IServices.IClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controlador para gestionar las operaciones relacionadas con los clientes.
 *
 * @author Guillermo Ramirez
 */
@RestController
@RequestMapping("/clientes")
public class ClienteController {

    @Autowired
    private IClienteService clienteService;

    /**
     * Obtiene un cliente por su identificador único.
     *
     * @param id El identificador del cliente.
     * @return Una respuesta que contiene el cliente encontrado y el estado HTTP. Si el cliente no se encuentra, se
     * devuelve un estado HTTP 404.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Cliente> obtenerCliente(@PathVariable String id) {
        try {
            Cliente cliente = clienteService.obtenerClientPorId(id);
            return new ResponseEntity<>(cliente, HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(new Cliente(), HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Guarda un nuevo cliente en el sistema.
     *
     * @param cliente El cliente a guardar.
     * @return Una respuesta que contiene el nuevo cliente creado y el estado HTTP 201.
     */
    @PostMapping
    public ResponseEntity<Cliente> guardaCliente(@RequestBody Cliente cliente) {
        Cliente nuevoCliente = clienteService.guardarCliente(cliente);
        return new ResponseEntity<>(nuevoCliente, HttpStatus.CREATED);
    }
}
