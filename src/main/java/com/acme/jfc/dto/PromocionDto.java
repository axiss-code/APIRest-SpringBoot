package com.acme.jfc.dto;

import com.acme.jfc.entity.Promocion;
import java.io.Serializable;
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
public class PromocionDto implements Serializable {
    
    private static final long serialVersionUID = 30101L;
    
    @NotEmpty
    private String promocionId;
    private String nombre;
    @DecimalMin(value = "0.0")
    @DecimalMax(value = "100.0")
    private Double descuento;
    
    // convert DTO to Entity
    public static Promocion mapDtoToEntity(Promocion promocion, PromocionDto dto) {
        Promocion p = promocion;
        p.setId(dto.getPromocionId());
        if (dto.getNombre() == null) {
            p.setNombre(promocion.getNombre() == null ? dto.getPromocionId() : promocion.getNombre());
        } else {
            p.setNombre(dto.getNombre());
        }
        p.setDescuento(dto.getDescuento() == null ? promocion.getDescuento() : dto.getDescuento());
        return p;
    }    
}
