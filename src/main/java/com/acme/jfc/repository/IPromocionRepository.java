package com.acme.jfc.repository;

import com.acme.jfc.entity.Prenda;
import com.acme.jfc.entity.Promocion;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface IPromocionRepository extends JpaRepository<Promocion, String>{
    @Query("select pda from Prenda pda where pda.id=:id")
    Optional <Prenda> verify (@Param("id") String id);
    
    @Modifying
    @Query(value= "delete from rel_prendas_promos where promocion_id = :id", nativeQuery = true)
    void deleteRelacionPromosPrenda (@Param("id") String id);
}

