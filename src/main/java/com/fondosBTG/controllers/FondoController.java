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

@RestController
@RequestMapping("/fondos")
@CrossOrigin(origins = "*")
public class FondoController {
    @Autowired
    private IFondoService fondoService;

    // Obtener todos los fondos
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
