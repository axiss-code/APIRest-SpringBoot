package com.acme.jfc.dto;

import com.acme.jfc.entity.Categoria;
import com.acme.jfc.entity.Prenda;

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
public class PrendaDto implements Serializable{
	
    private static final long serialVersionUID = 20101L;

    @Pattern(regexp = "^[SML][a-zA-Z0-9]{9}$", message = "prendaId debe ser S|M|L + 9 alfanumericos (length=10) ej S1234567A9")
    private String prendaId;
    @DecimalMin(value = "0.0")
    private Double precio;
    private Set<Categoria> categorias;
    
    // convert DTO to Entity
    public static Prenda mapDtoToEntity(Prenda prenda, PrendaDto dto) {
        Prenda p = prenda;
        p.setPrecio(dto.getPrecio() == null ? prenda.getPrecio() : Math.round(dto.getPrecio()*100.0)/100.0);
        p.setCategorias(dto.getCategorias() == null ? prenda.getCategorias() : dto.getCategorias());
        return p;
    }
}
