package com.fondosBTG.controllers;

import com.fondosBTG.dto.AperturaPeticionDTO;
import com.fondosBTG.exception.OperationNotAllowedException;
import com.fondosBTG.exception.ResourceNotFoundException;
import com.fondosBTG.models.Transaccion;
import com.fondosBTG.services.IServices.ITransaccionService;
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

import java.util.List;

/**
 * Controlador para gestionar las operaciones relacionadas con las transacciones de fondos.
 *
 * @author Guillermo Ramirez
 */
@RestController
@RequestMapping("/transacciones")
@CrossOrigin(origins = "*")
public class TransaccionController {

    @Autowired
    private ITransaccionService transaccionService;

    /**
     * Abre un fondo para un cliente específico.
     *
     * @param clienteId       ID del cliente.
     * @param fondoId         ID del fondo.
     * @param aperturaRequest Objeto que contiene la solicitud de apertura.
     * @return Una respuesta que contiene el resultado de la operación y el estado HTTP. Puede devolver un estado HTTP
     * 201 (CREATED), 404 (NOT FOUND) o 400 (BAD REQUEST) en caso de error.
     */
    @PostMapping("/apertura/{clienteId}/{fondoId}")
    public ResponseEntity<String> abrirFondo(
            @PathVariable String clienteId,
            @PathVariable String fondoId,
            @RequestBody AperturaPeticionDTO aperturaRequest) {
        try {
            double monto = aperturaRequest.getMonto();
            String canalNotificacion = aperturaRequest.getCanalNotificacion();
            String resultado = transaccionService.realizarApertura(clienteId, fondoId, monto, canalNotificacion);
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

    /**
     * Cancela un fondo para un cliente específico.
     *
     * @param clienteId     ID del cliente.
     * @param fondoId       ID del fondo.
     * @param transaccionId ID de la transacción a cancelar.
     * @return Una respuesta que contiene el resultado de la operación y el estado HTTP. Puede devolver un estado HTTP
     * 200 (OK), 404 (NOT FOUND) o 400 (BAD REQUEST) en caso de error.
     */
    @PostMapping("/cancelacion/{clienteId}/{fondoId}/{transaccionId}")
    public ResponseEntity<String> cancelarFondo(
            @PathVariable String clienteId,
            @PathVariable String fondoId,
            @PathVariable String transaccionId) {
        try {
            String resultado = transaccionService.realizarCancelacion(clienteId, fondoId, transaccionId);
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

    /**
     * Obtiene el historial de transacciones de un cliente específico.
     *
     * @param clienteId ID del cliente.
     * @return Una respuesta que contiene la lista de transacciones y el estado HTTP. Puede devolver un estado HTTP 200
     * (OK), 404 (NOT FOUND) o 500 (INTERNAL SERVER ERROR) en caso de error.
     */
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
