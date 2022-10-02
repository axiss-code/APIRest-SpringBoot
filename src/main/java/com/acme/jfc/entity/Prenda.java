package com.acme.jfc.entity;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Pattern;
import com.fasterxml.jackson.annotation.JsonBackReference;
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
@Table(name = "prendas")
public class Prenda implements Serializable {

    private static final long serialVersionUID = 12321L;

    @Id
    @Pattern(regexp = "^[SML][a-zA-Z0-9]{9}$", message = "ID must be S|M|L + 9 alphanumerics length :: S1234567B8")
    @Column(name = "prenda_id", length = 10, columnDefinition = "VARCHAR(10)")
    private String id;

    @Column(name = "precio", columnDefinition = "Decimal(6,2) default '0.0'")
    @DecimalMin(value = "0.0")
    private double precio;

    @JoinTable(name = "rel_prendas_categorias")
    @ElementCollection(targetClass = Categoria.class)
    private Set<Categoria> categorias;
    
    @JsonBackReference
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "rel_prendas_promos",
            joinColumns = {@JoinColumn(name = "prenda_id", nullable = false)},
            inverseJoinColumns = {@JoinColumn(name = "promocion_id", nullable = false)})
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Set<Promocion> promos = new HashSet<>();
}
