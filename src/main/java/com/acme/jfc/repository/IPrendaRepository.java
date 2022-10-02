package com.acme.jfc.repository;

import com.acme.jfc.entity.Prenda;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface IPrendaRepository extends JpaRepository<Prenda, String> {
    @Modifying
    @Query(value= "delete from rel_prendas_promos where prenda_id = :id", nativeQuery = true)
    void deleteRelacionPrendaPromos (@Param("id") String id);
    
}
