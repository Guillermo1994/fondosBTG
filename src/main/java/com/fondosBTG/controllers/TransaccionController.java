package com.fondosBTG.controllers;

import com.fondosBTG.exception.OperationNotAllowedException;
import com.fondosBTG.exception.ResourceNotFoundException;
import com.fondosBTG.models.Transaccion;
import com.fondosBTG.services.IServices.ITransaccionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/transacciones")
public class TransaccionController {

    @Autowired
    private ITransaccionService transaccionService;

    // Abrir un fondo
    @PostMapping("/apertura/{clienteId}/{fondoId}")
    public ResponseEntity<String> abrirFondo(
            @PathVariable String clienteId,
            @PathVariable String fondoId,
            @RequestParam double monto) {
        try {
            String resultado = transaccionService.realizarApertura(clienteId, fondoId, monto);
            return new ResponseEntity<>(resultado, HttpStatus.CREATED);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (OperationNotAllowedException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>("Error al realizar la apertura del fondo.",
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Cancelar un fondo
    @PostMapping("/cancelacion/{clienteId}/{fondoId}")
    public ResponseEntity<String> cancelarFondo(
            @PathVariable String clienteId,
            @PathVariable String fondoId) {
        try {
            String resultado = transaccionService.realizarCancelacion(clienteId, fondoId);
            return new ResponseEntity<>(resultado, HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (OperationNotAllowedException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>("Error al realizar la cancelación del fondo.",
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Ver historial de transacciones
    @GetMapping("/historial/{clienteId}")
    public ResponseEntity<List<Transaccion>> verHistorial(@PathVariable String clienteId) {
        List<Transaccion> historial = null;
        try {
            historial = transaccionService.verHistorial(clienteId);
            if (historial.isEmpty()) {
                throw new ResourceNotFoundException("No se encontraron transacciones para el cliente con ID: "
                        + clienteId);
            }
            return new ResponseEntity<>(historial, HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(historial, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(historial, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
