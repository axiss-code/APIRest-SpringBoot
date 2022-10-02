package com.acme.jfc.dto;

import java.io.Serializable;
import java.util.Set;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PromocionRebajasDto implements Serializable {
    
    private static final long serialVersionUID = 30101L;
    
    @NotEmpty
    private String promocionId;
    private String nombre;
    @DecimalMin(value = "0.0")
    @DecimalMax(value = "100.0")
    private Double descuento;
    private Set<PrendaRebajasDto> prendas;
}
