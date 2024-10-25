package com.fondosBTG.services.IServices;

import com.fondosBTG.models.Fondo;

import java.util.List;

public interface IFondoService {

    List<Fondo> obtenerFondos();

    Fondo obtenerFondoPorId(String id);

    Fondo guardarFondo(Fondo fondo);
}
