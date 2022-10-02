package com.acme.jfc.service.impl;

import com.acme.jfc.entity.Prenda;
import com.acme.jfc.exception.ResourceNotFoundException;
import com.acme.jfc.repository.IPrendaRepository;
import com.acme.jfc.service.IPrendaService;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class PrendaServiceImpl implements IPrendaService{

    @Autowired
    private IPrendaRepository prendaRepository;

    @Override
    public Prenda create(Prenda prenda) {
    	return (findById(prenda.getId()).isPresent()) ? null : prendaRepository.save(prenda);
    }

    @Override
    public Prenda update(String id, Prenda prenda) {
    	Prenda existePrenda = prendaRepository.findById(id).orElseThrow(
    			() -> new ResourceNotFoundException(id));
    	existePrenda.setPrecio(prenda.getPrecio());
    	existePrenda.setCategorias(prenda.getCategorias());
        return prendaRepository.save(existePrenda);
    }

    @Override
    @Transactional(readOnly = true)
    public Prenda getById(String id) {
    	return prendaRepository.findById(id).orElseThrow(
    			() -> new ResourceNotFoundException(id));
    }

    @Override
    public void deleteById(String id) {
    	prendaRepository.findById(id).orElseThrow(
    			() -> new ResourceNotFoundException(id));
        prendaRepository.deleteRelacionPrendaPromos(id);
    	prendaRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Prenda> getAll() {
         return prendaRepository.findAll();
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<Prenda> findById(String id) {
    	return prendaRepository.findById(id);
    }
    
}
