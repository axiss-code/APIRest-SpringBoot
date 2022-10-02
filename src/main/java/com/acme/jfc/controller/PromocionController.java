package com.acme.jfc.controller;

import com.acme.jfc.dto.PrendaPromocionadaDto;
import com.acme.jfc.dto.PromocionDto;
import com.acme.jfc.dto.PromocionRebajasDto;
import com.acme.jfc.entity.Promocion;
import com.acme.jfc.service.IPromocionService;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/promociones")
public class PromocionController {
    
    @Autowired
    private IPromocionService promocionService;
    
    @Autowired
    private ModelMapper mapper;
     
    /* GET /api/v1/promociones 
     *      Para una List de objetos PromocionDto (id, nombre, descuento)
     * GET /api/v1/promociones?full=true
     *      Para una List de objetos PromocionRebajasDto (id, nombre, descuento, prendas)
     * GET /api/v1/promociones?search=B
     *      Para una List de objetos PromocionDto que contenga el nombre B (id, nombre, descuento)
     * GET /api/v1/promociones?search=B&full=true
     *      Para una List de objetos PromocionRebajasDto que contenga el nombre B (id, nombre, descuento, prendas)
    */
    @GetMapping
    @ApiOperation(
            value = "Genera un Listado de todas las promociones.",
            responseContainer = "List",
            notes = "(opcional) ?full=true -> muestra prendas asociadas además del id, nombre y descuento."
                  + "(opcional) ?search=text -> filtra el listado por nombre que contenga 'text'.")
    ResponseEntity<?> getAll(@RequestParam(defaultValue = "false", required = false) Boolean full,
                             @RequestParam(defaultValue = "", required = false) String search){
        List<?> pList;
        if(!full) {
            pList = promocionService.getAll().stream().filter(e -> e.getNombre().contains(search))
                    .map(p -> mapper.map(p, PromocionDto.class)).collect(Collectors.toList());
        } else {
            pList = promocionService.getAll().stream().filter(e -> e.getNombre().contains(search))
                    .map(p -> mapper.map(p, PromocionRebajasDto.class)).collect(Collectors.toList());
        }
        return ResponseEntity.ok(pList);
    }

    //GET api/v1/promociones/BF2022
    //Devulve una promocion y todos sus campos. PromocionRebajasDto
    @GetMapping("{id}")
    @ApiOperation(
            value = "Información detallada de una promoción según su id.",
            response = PromocionRebajasDto.class,
            notes = "Respuesta: 404 cuando el id no exista.")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Id encontrado."),
        @ApiResponse(code = 404, message = "Id de promocion no encontrado.")})
    public ResponseEntity<?> getById (@PathVariable("id") @Valid String id) {
        return new ResponseEntity<PromocionRebajasDto>(mapper.map(promocionService.getById(id), PromocionRebajasDto.class), HttpStatus.OK);
    }
    
    /* PUT - /api/v1/promociones/BF2022
     * El RequestBody acepta un objeto PromocionDto. Omitirá otros valores que se ingresen diferentes
     * al Nombnre y/o Descuento.
    */
    @PutMapping("{id}")
    @ApiOperation(
        value = "Modifica una promoción por su id",
        response = PromocionDto.class,
        notes = "Respuestas: 400 si el body detecta errores de validación(@Valid). 404 cuando el id no exista.")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Id y body correctos. Success."),
        @ApiResponse(code = 400, message = "Estructura no válida en el body."),
        @ApiResponse(code = 404, message = "Id no encontrado.")})
    public ResponseEntity<?> update (@PathVariable("id") @Valid String id, @RequestBody @Valid PromocionDto promoDto) {
        Optional<Promocion> optPromo = promocionService.findById(id);
        if (optPromo.isPresent()) {
            Promocion p = promocionService.update(id, PromocionDto.mapDtoToEntity(optPromo.get(), promoDto));
            return new ResponseEntity<PromocionDto>(mapper.map(p, PromocionDto.class), HttpStatus.OK);
        } else {
            String msg ="{ \"status\": 404, \"message\": \"Elemento no encontrado: " + id + "\" }";
            JsonObject json = new Gson().fromJson(msg, JsonObject.class);
            return new ResponseEntity<String>(json.toString(), HttpStatus.NOT_FOUND);
       }
    }

    //Delete /api/v1/promociones/BF2022
    @DeleteMapping("{id}")
    @ApiOperation(
        value = "Elimina una promoción por su id, y sus posibles asociaciones a Prendas",
        notes = "Respuesta: 404 cuando el id no exista.")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Id encontrado. Success."),
        @ApiResponse(code = 404, message = "Id no encontrado.")})
    public ResponseEntity<String> deleteByid (@PathVariable("id") @Valid String id) {
        if (promocionService.findById(id).isPresent()) {
            promocionService.deleteById(id);
            String msg ="{ \"status\": 200, \"message\": \"Promocion borrada con éxito!\" }";
            JsonObject json = new Gson().fromJson(msg, JsonObject.class);
            return new ResponseEntity<String>(json.toString(), HttpStatus.OK);
        } else {
            String msg ="{ \"status\": 404, \"message\": \"Elemento no encontrado: " + id + "\" }";
            JsonObject json = new Gson().fromJson(msg, JsonObject.class);
            return new ResponseEntity<String>(json.toString(), HttpStatus.NOT_FOUND);
        }  
    }

    /* POST /api/v1/promociones
     * El RequestBody acepta un objeto PrendaDto. Aisgnará "promocionId", "nombre" y "descuento",
     * y omitirá otros campos como prendas asociadas.
    */
    @PostMapping()
    @ApiOperation(
        value = "Añade una nueva promoción a la BD.",
        response = PromocionDto.class,
        notes = "Respuesta: 400 si el body detecta errores de validación(@Valid) o cuando el id ya exista.")
    @ApiResponses(value = {
        @ApiResponse(code = 201, message = "Body correcto. Success."),
        @ApiResponse(code = 400, message = "Estructura del Body no válida o Id duplicado.")})
    public ResponseEntity<?> create (@RequestBody @Valid PromocionDto promoDto) {
    	if (promocionService.findById(promoDto.getPromocionId()).isPresent()) {
            String msg ="{ \"status\": 400, \"message\": \"Ya existe una promocion con el identificador: "+ promoDto.getPromocionId()+"\" }";
            JsonObject json = new Gson().fromJson(msg, JsonObject.class);
            return new ResponseEntity<String>(json.toString(), HttpStatus.BAD_REQUEST);
    	} else {
            Promocion p = PromocionDto.mapDtoToEntity(new Promocion(), promoDto); // de DTO al formato de la Entidad
            promocionService.create(p);
            PromocionDto pDto = mapper.map(p, PromocionDto.class); // Y si hubo éxito, proceso inverso Entidad a DTO hacia el Body
            return new ResponseEntity<PromocionDto>(pDto, HttpStatus.CREATED);
    	}
    }
    
    //PUT /api/v1/promociones/aplicar?promocion=BF2022&prenda=S123456789
    //Asocia una promoción a una única prenda
    @PutMapping("/aplicar")
    @ApiOperation(
        value = "Asocia una promoción a una única prenda",
        response = PrendaPromocionadaDto.class,
        notes = "Respuesta: 400 cuando no exista alguno de los ids (prenda o promoción).")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Ids correctos. Success."),
        @ApiResponse(code = 400, message = "No se encuentra al menos un id.")})
    public ResponseEntity<?> applyPromo (@RequestParam (name="promocion", required = true) @Valid String promoId,
                                         @RequestParam (name="prenda", required = true) @Valid String prendaId) {
        Promocion p = promocionService.applyPromo(promoId, prendaId);
        if (p == null) {
            String msg = "{ \"status\": 400, \"message\": \"Acción no realizada. Revise los ids.\" }";
            JsonObject json = new Gson().fromJson(msg, JsonObject.class);
            return new ResponseEntity<String>(json.toString(), HttpStatus.BAD_REQUEST);
        } else {
            return new ResponseEntity<PrendaPromocionadaDto>(mapper.map(promocionService.verify(prendaId).get(), PrendaPromocionadaDto.class), HttpStatus.OK);
        }
    }
    
    //PUT /api/v1/promociones/remover?promocion=BF2022&prenda=S123456789
    //Desaplica una promoción a una única prenda
    @PutMapping("/remover")
    @ApiOperation(
        value = "Desaplica una promoción a una única prenda",
        notes = "Respuesta: 404 cuando no exista alguno de los ids (prenda o promoción).")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Ids correctos. Success."),
        @ApiResponse(code = 404, message = "No se encuentra al menos un id.")})
    public ResponseEntity<?> removePromo (@RequestParam (name="promocion", required = true) @Valid String promoId,
                                          @RequestParam (name="prenda", required = true) @Valid String prendaId) {
        boolean serviceResponse = promocionService.removePromo(promoId, prendaId);
        String msg;
        JsonObject json;
        HttpStatus status;
        if (serviceResponse) {
            msg = "{ \"status\": 200, \"message\": \"Acción efectuada sin errores.\" }";
            status = HttpStatus.OK;
            json = new Gson().fromJson(msg, JsonObject.class); 
        } else {
            msg = "{ \"status\": 400, \"message\": \"Acción no realizada. Revise los ids.\" }";
            status = HttpStatus.NOT_FOUND;
            json = new Gson().fromJson(msg, JsonObject.class);
        }
        return new ResponseEntity<String>(json.toString(), status);
    }
    
    //PUT /api/v1/promociones/{promocionId}/prendas
    //Asigna productos a una promoción existente. Todas las prendas debe existir.
    @PutMapping("/{id}/prendas")
    @ApiOperation(
        value = "Asocia un Array de prendas a una promoción",
        notes = "Respuesta: 400 cuando no exista alguno de los ids (prenda o promoción).")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Ids correctos. Success."),
        @ApiResponse(code = 400, message = "No se encuentra al menos un id.")})
    public ResponseEntity<?> applyPromoList (@PathVariable("id") @Valid String id, @RequestBody List<String> listado) {
        if (promocionService.findById(id).isPresent()) {
            for(String ids:listado) {
                if(promocionService.verify(ids).isEmpty()) {
                    String msg = "{ \"status\": 400, \"message\": \"Acción no realizada. Revise array de [Prendas].\" }";
                    JsonObject json = new Gson().fromJson(msg, JsonObject.class);
                    return new ResponseEntity<String>(json.toString(), HttpStatus.BAD_REQUEST);
                }
            }
            int contador=0;
            for(String ids:listado) {
                promocionService.applyPromo(id, ids);
                contador++;
            }
            String msg = "{ \"status\": 200, \"message\": \""+contador+" prendas asignadas.\" }";
            JsonObject json = new Gson().fromJson(msg, JsonObject.class);
            return new ResponseEntity<String>(json.toString(), HttpStatus.OK);
        } else {
            String msg ="{ \"status\": 404, \"message\": \"Elemento no encontrado: " + id + "\" }";
            JsonObject json = new Gson().fromJson(msg, JsonObject.class);
            return new ResponseEntity<String>(json.toString(), HttpStatus.NOT_FOUND);
        }
    }
    
    //DELETE /api/v1/promociones/{promocionId}/prendas
    //Desasignar productos de una promoción existente. Todas las prendas debe existir.
    @DeleteMapping("/{id}/prendas")
    @ApiOperation(
        value = "Desaplica una array de prendas de una promoción.",
        notes = "Respuesta: 404 cuando no exista alguno de los ids (promoción o prendas).")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Ids correctos. Success."),
        @ApiResponse(code = 404, message = "No se encuentra al menos un id.")})
    public ResponseEntity<?> removePromoList (@PathVariable("id") @Valid String id, @RequestBody List<String> listado) {
        if (promocionService.findById(id).isPresent()) {
            for(String ids:listado) {
                if(promocionService.verify(ids).isEmpty()) {
                    String msg = "{ \"status\": 400, \"message\": \"Acción no realizada. Revise array de [Prendas].\" }";
                    JsonObject json = new Gson().fromJson(msg, JsonObject.class);
                    return new ResponseEntity<String>(json.toString(), HttpStatus.BAD_REQUEST);
                }
            }
            int contador=0;
            for(String ids:listado) {
                promocionService.removePromo(id, ids);
                contador++;
            }
            String msg = "{ \"status\": 200, \"message\": \""+contador+" prendas asignadas.\" }";
            JsonObject json = new Gson().fromJson(msg, JsonObject.class);
            return new ResponseEntity<String>(json.toString(), HttpStatus.OK);
        } else {
            String msg ="{ \"status\": 404, \"message\": \"Elemento no encontrado: " + id + "\" }";
            JsonObject json = new Gson().fromJson(msg, JsonObject.class);
            return new ResponseEntity<String>(json.toString(), HttpStatus.NOT_FOUND);
        }
    }
  
}
