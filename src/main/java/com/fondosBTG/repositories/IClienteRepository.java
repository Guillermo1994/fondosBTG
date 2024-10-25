package com.fondosBTG.repositories;

import com.fondosBTG.models.Cliente;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface IClienteRepository extends MongoRepository<Cliente, String> {
}
