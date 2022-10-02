package com.acme.jfc.service;

import com.acme.jfc.entity.Prenda;
import com.acme.jfc.entity.Promocion;
import java.util.List;
import java.util.Optional;

public interface IPromocionService {
    Promocion create (Promocion promocion);
    Promocion update (String id, Promocion promocion);
    Promocion getById (String id);
    void deleteById (String id);
    List<Promocion> getAll();
    Optional<Promocion> findById (String id);
    Optional <Prenda> verify (String id);
    
    Promocion applyPromo (String promoId, String prendaId);
    boolean removePromo (String promoId, String prendaId);
}
