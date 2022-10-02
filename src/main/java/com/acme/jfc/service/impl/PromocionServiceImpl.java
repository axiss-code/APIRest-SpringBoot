package com.acme.jfc.service.impl;

import com.acme.jfc.entity.Prenda;
import com.acme.jfc.entity.Promocion;
import com.acme.jfc.exception.ResourceNotFoundException;
import com.acme.jfc.repository.IPromocionRepository;
import com.acme.jfc.service.IPromocionService;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class PromocionServiceImpl implements IPromocionService {
    
    @Autowired
    private IPromocionRepository promocionRepository;
    
    @Override
    @Transactional(readOnly = true)
    public Optional<Promocion> findById(String id) {
        return promocionRepository.findById(id);
    }
    
    @Override
    public Promocion create(Promocion promocion) {
        return (findById(promocion.getId()).isPresent()) ? null : promocionRepository.save(promocion);
    }

    @Override
    public Promocion update(String id, Promocion promocion) {
        Promocion existePromocion = promocionRepository.findById(id).orElseThrow(
    			() -> new ResourceNotFoundException(id));
    	existePromocion.setNombre(promocion.getNombre());
    	existePromocion.setDescuento(promocion.getDescuento());
        return promocionRepository.save(existePromocion);
    }

    @Override
    @Transactional(readOnly = true)
    public Promocion getById(String id) {
        return promocionRepository.findById(id).orElseThrow(
                    () -> new ResourceNotFoundException(id));
    }
    
    @Override
    @Transactional
    public void deleteById(String id){
        Promocion p = promocionRepository.findById(id).orElseThrow(
    			() -> new ResourceNotFoundException(id));
        promocionRepository.deleteRelacionPromosPrenda(id);
    	promocionRepository.deleteById(id);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Promocion> getAll() {
        return promocionRepository.findAll();
    }
    
    @Override
    public Promocion applyPromo (String promoId, String prendaId) {
        Optional<Promocion> existePromocion = promocionRepository.findById(promoId);
        Optional<Prenda> existePrenda = promocionRepository.verify(prendaId);
        if(existePromocion.isPresent() && existePrenda.isPresent()) {
            existePromocion.get().getPrendas().add(existePrenda.get());
            existePrenda.get().getPromos().add(existePromocion.get());
            return promocionRepository.save(existePromocion.get());
        } else {
            return null;
        }
    }
  
    @Override
    public boolean removePromo (String promoId, String prendaId) {
        Optional<Promocion> existePromocion = promocionRepository.findById(promoId);
        Optional<Prenda> existePrenda = promocionRepository.verify(prendaId);
        if(existePromocion.isPresent() && existePrenda.isPresent()) {
            existePromocion.get().getPrendas().remove(existePrenda.get());
            existePrenda.get().getPromos().remove(existePromocion.get());
            promocionRepository.save(existePromocion.get());
            return true;
        } else {
            return false;
        }
    }
    
    @Override
    public Optional<Prenda> verify(String id) {
        return promocionRepository.verify(id);
    }
}
