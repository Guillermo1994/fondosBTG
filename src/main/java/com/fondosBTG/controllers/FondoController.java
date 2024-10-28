package com.fondosBTG.controllers;

import com.fondosBTG.models.Fondo;
import com.fondosBTG.services.IServices.IFondoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Controlador para gestionar las operaciones relacionadas con los fondos.
 *
 * @author Guillermo Ramirez
 */
@RestController
@RequestMapping("/fondos")
@CrossOrigin(origins = "*")
public class FondoController {

    @Autowired
    private IFondoService fondoService;

    /**
     * Obtiene todos los fondos disponibles en el sistema.
     *
     * @return Una respuesta que contiene la lista de fondos y el estado HTTP. Si ocurre un error, se devuelve un estado
     * HTTP 500.
     */
    @GetMapping
    public ResponseEntity<List<Fondo>> obtenerFondos() {
        List<Fondo> fondos = null;
        try {
            fondos = fondoService.obtenerFondos();
            return new ResponseEntity<>(fondos, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(fondos, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Guarda un nuevo fondo en el sistema.
     *
     * @param fondo El fondo a guardar.
     * @return Una respuesta que contiene el nuevo fondo creado y el estado HTTP 201. Si hay un error en la solicitud,
     * se devuelve un estado HTTP 400.
     */
    @PostMapping
    public ResponseEntity<Fondo> guardaFondo(@RequestBody Fondo fondo) {
        try {
            Fondo nuevoFondo = fondoService.guardarFondo(fondo);
            return new ResponseEntity<>(nuevoFondo, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(new Fondo(), HttpStatus.BAD_REQUEST);
        }
    }
}
