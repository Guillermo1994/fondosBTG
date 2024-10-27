package com.fondosBTG.controllers;

import com.fondosBTG.exception.ResourceNotFoundException;
import com.fondosBTG.models.Cliente;
import com.fondosBTG.services.IServices.IClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/clientes")
@CrossOrigin(origins = "*")
public class ClienteController {
    @Autowired
    private IClienteService clienteService;

    @GetMapping("/{id}")
    public ResponseEntity<Cliente> obtenerCliente(@PathVariable String id) {
        try {
            Cliente cliente = clienteService.obtenerClientPorId(id);
            return new ResponseEntity<>(cliente, HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(new Cliente(), HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping
    public ResponseEntity<Cliente> guardaCliente(@RequestBody Cliente cliente) {
        Cliente nuevoCliente = clienteService.guardarCliente(cliente);
        return new ResponseEntity<>(nuevoCliente, HttpStatus.CREATED);
    }
}
