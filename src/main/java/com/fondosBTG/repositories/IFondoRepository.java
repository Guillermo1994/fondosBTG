package com.fondosBTG.repositories;

import com.fondosBTG.models.Fondo;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface IFondoRepository extends MongoRepository<Fondo, String> {
}
