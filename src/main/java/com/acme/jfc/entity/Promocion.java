package com.acme.jfc.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="promociones")
public class Promocion implements Serializable {
    
    private static final long serialVersionUID = 12322L;
    
    @Id
    @Column(name = "promocion_id")
    private String id;

    @Column(name="campaña", nullable=true, columnDefinition = "varchar(50) default '${id}'")
    private String nombre;

    @Column(name="descuento", columnDefinition="Decimal(5,2) default '0.00'")
    @DecimalMin(value = "0.0")
    @DecimalMax(value = "100.0")
    private double descuento;

    @JsonBackReference
    @ManyToMany(mappedBy = "promos", fetch = FetchType.LAZY)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Set<Prenda> prendas = new HashSet<>(); //Relacionado con Set<Promocion> promos -> en entidad Prenda
    
}
