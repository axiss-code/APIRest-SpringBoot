package com.acme.jfc.configuration;

import com.acme.jfc.entity.Categoria;
import com.acme.jfc.entity.Prenda;
import com.acme.jfc.entity.Promocion;
import com.acme.jfc.repository.IPrendaRepository;
import com.acme.jfc.repository.IPromocionRepository;
import java.util.HashSet;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements ApplicationRunner{
    
    @Autowired
    private IPrendaRepository prendaRepository;
    
    @Autowired
    private IPromocionRepository promocionRepository;

    @Override
    public void run(ApplicationArguments args) {
        Prenda p = new Prenda();
        p.setId("S123456789");
        p.setPrecio(10.0);
        Set<Categoria> cats=new HashSet<>();
        cats.add(Categoria.Mujer);
        cats.add(Categoria.Zapatos);
        p.setCategorias(cats);
        prendaRepository.save(p);
        
        p = new Prenda();
        p.setId("S12345ABCD");
        p.setPrecio(15.5);
        prendaRepository.save(p);
        
        p = new Prenda();
        p.setId("L123456789");
        p.setPrecio(5);
        cats.clear();
        cats.add(Categoria.Hombre);
        cats.add(Categoria.Zapatos);
        p.setCategorias(cats);
        prendaRepository.save(p);
        
        p = new Prenda();
        p.setId("S987654321");
        p.setPrecio(12.12);
        cats.clear();
        cats.add(Categoria.Hombre);
        p.setCategorias(cats);
        prendaRepository.save(p);
        
        Promocion n = new Promocion();
        n.setId("BF2022");
        n.setNombre("Black Friday 2022");
        n.setDescuento(40);
        promocionRepository.save(n);
        
        n = new Promocion();
        n.setId("NAV22");
        n.setNombre("Campaña Navidad 2022");
        n.setDescuento(10.5);
        promocionRepository.save(n);
        
        n = new Promocion();
        n.setId("REBAJAS");
        n.setNombre("Rebajas Verano");
        n.setDescuento(15.0);
        promocionRepository.save(n);
        
    }
    
}
