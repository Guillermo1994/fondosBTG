package com.fondosBTG.services;

import com.fondosBTG.exception.ResourceNotFoundException;
import com.fondosBTG.models.Fondo;
import com.fondosBTG.repositories.IFondoRepository;
import com.fondosBTG.services.IServices.IFondoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FondoServiceImpl implements IFondoService {
    @Autowired
    private IFondoRepository fondoRepository;

    @Override
    public List<Fondo> obtenerFondos() {
        return (List<Fondo>) fondoRepository.findAll();
    }

    @Override
    public Fondo obtenerFondoPorId(String id) {
        // Si no se encuentra el fondo, se lanza una excepción personalizada
        return fondoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Fondo con ID " + id + " no encontrado."));
    }

    @Override
    public Fondo guardarFondo(Fondo fondo) {
        return fondoRepository.save(fondo);
    }
}
