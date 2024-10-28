package com.fondosBTG.services;

import com.fondosBTG.exception.ResourceNotFoundException;
import com.fondosBTG.models.Fondo;
import com.fondosBTG.repositories.IFondoRepository;
import com.fondosBTG.services.IServices.IFondoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Implementación del servicio para la gestión de fondos.
 *
 * @author Guillermo Ramirez
 */
@Service
public class FondoServiceImpl implements IFondoService {

    @Autowired
    private IFondoRepository fondoRepository;

    /**
     * Obtiene la lista de todos los fondos disponibles.
     *
     * @return una lista de objetos Fondo que representa todos los fondos en el repositorio.
     */
    @Override
    public List<Fondo> obtenerFondos() {
        return (List<Fondo>) fondoRepository.findAll();
    }

    /**
     * Obtiene un fondo específico basado en su ID.
     *
     * @param id el ID del fondo a obtener.
     * @return el fondo correspondiente al ID especificado.
     * @throws ResourceNotFoundException si no se encuentra un fondo con el ID especificado.
     */
    @Override
    public Fondo obtenerFondoPorId(String id) {
        return fondoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Fondo con ID " + id + " no encontrado."));
    }

    /**
     * Guarda un fondo en el repositorio.
     *
     * @param fondo el objeto Fondo que se desea guardar.
     * @return el fondo guardado en el repositorio.
     */
    @Override
    public Fondo guardarFondo(Fondo fondo) {
        return fondoRepository.save(fondo);
    }
}
