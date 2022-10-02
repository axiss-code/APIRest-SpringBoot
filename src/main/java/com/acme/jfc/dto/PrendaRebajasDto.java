package com.acme.jfc.dto;

import com.acme.jfc.entity.Categoria;
import com.acme.jfc.entity.Promocion;
import com.fasterxml.jackson.annotation.JsonBackReference;
import java.io.Serializable;
import java.util.Set;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PrendaRebajasDto implements Serializable {
    
    private static final long serialVersionUID = 20109L;
    
    @Pattern(regexp = "^[SML][a-zA-Z0-9]{9}$", message = "prendaId debe ser S|M|L + 9 alfanumericos (length=10) ej S1234567A9")
    private String prendaId;
    @DecimalMin(value = "0.0")
    private Double precio;
    private Double precio_promocional;
    private Set<Categoria> categorias;
    @JsonBackReference
    private Set<Promocion> promos;

    public double getPrecio_promocional() {
        calculatePVP();
        return precio_promocional;
    }

    public void setPrecio_promocional() {
        calculatePVP();
    }
    
    //Calcula Precio Promocional
    private void calculatePVP () {
        this.precio_promocional = this.precio;
        if (promos != null && promos.size() != 0) {
            for(Promocion p : promos){
                this.precio_promocional = this.precio_promocional * ((100-p.getDescuento())/100);
            }
        }
    }
    
}