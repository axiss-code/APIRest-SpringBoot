package com.acme.jfc.service;

import com.acme.jfc.entity.Prenda;
import java.util.List;
import java.util.Optional;

public interface IPrendaService {
    Prenda create (Prenda prenda);
    Prenda update (String id, Prenda prenda);
    Prenda getById (String id);
    void deleteById (String id);
    List<Prenda> getAll();
    Optional<Prenda> findById (String id);
}
