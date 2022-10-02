package com.acme.jfc.controller;

import com.acme.jfc.dto.PrendaPromocionadaDto;
import com.acme.jfc.dto.PrendaDto;
import com.acme.jfc.entity.Prenda;
import com.acme.jfc.service.IPrendaService;

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
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/api/v1/prendas")
public class PrendaController {
    
    @Autowired
    private IPrendaService prendaService;
    
    @Autowired
    private ModelMapper mapper;
    
    /* GET /api/v1/prendas 
     *      Para una List de objetos PrendaDto (id, precio, categorias)
     * GET /api/v1/prendas?full=true
     *       Para una List de objetos PrendaPromocionadaDto (id, precio, precio_promocional, categorias, promociones)
    */
    @ApiOperation(
            value = "Genera un Listado de todas las prendas.",
            responseContainer = "List",
            notes = "(opcional) ?full=true -> para ver además el precio promocional y promociones asociadas.")
    @GetMapping
    public ResponseEntity<?> getAll(@RequestParam(defaultValue = "false", required = false) Boolean full){
        List<?> pList;
        if(!full) {
            pList = prendaService.getAll().stream().map(p -> mapper.map(p, PrendaDto.class)).collect(Collectors.toList());
        } else {
            pList = prendaService.getAll().stream().map(p -> mapper.map(p, PrendaPromocionadaDto.class)).collect(Collectors.toList());
        }
    	return ResponseEntity.ok(pList);
    }
    
    //GET /api/v1/prendas/S123456789
    //Devulve una prenda y todos sus campos. PrendaPromocionadaDto
    @GetMapping("{id}")
    @ApiOperation(
            value = "Información detallada de una prenda según su id.",
            response = PrendaPromocionadaDto.class,
            notes = "Respuesta: 404 cuando el id no exista.")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Id encontrado."),
        @ApiResponse(code = 404, message = "Id de prenda no encontrado.")})
    public ResponseEntity<?> getById (@PathVariable("id") @Valid String id) {
    	return new ResponseEntity<PrendaPromocionadaDto>(mapper.map(prendaService.getById(id), PrendaPromocionadaDto.class), HttpStatus.OK);
    }
    
    /* PUT - /api/v1/prendas/S123456789
     * El RequestBody acepta un objeto PrendaDto. Omitirá otros valores que se ingresen diferentes
     * al Precio y/o un Set<Categorias>.
    */
    @PutMapping("{id}")
    @ApiOperation(
        value = "Modifica una prenda por su id",
        response = PrendaDto.class,
        notes = "Respuestas: 400 si el body detecta errores de validación(@Valid). 404 cuando el id no exista.")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Id y body correctos. Success."),
        @ApiResponse(code = 400, message = "Estructura no válida en el body."),
        @ApiResponse(code = 404, message = "Id no encontrado.")})
    public ResponseEntity<?> update (@PathVariable("id") @Valid String id, @RequestBody @Valid PrendaDto prendaDto) {
    	Optional<Prenda> optPrenda = prendaService.findById(id);
        if (optPrenda.isPresent()) {
            Prenda p = prendaService.update(id, PrendaDto.mapDtoToEntity(optPrenda.get(), prendaDto));
            return new ResponseEntity<PrendaDto>(mapper.map(p, PrendaDto.class), HttpStatus.OK);
    	} else {
            String msg ="{ \"status\": 404, \"message\": \"Elemento no encontrado: " + id + "\" }";
            JsonObject json = new Gson().fromJson(msg, JsonObject.class);
            return new ResponseEntity<String>(json.toString(), HttpStatus.NOT_FOUND);
    	}
    }
    
    //Delete /api/v1/prendas/S123456789
    @DeleteMapping("{id}")
    @ApiOperation(
        value = "Elimina una prenda por su id, y sus posibles asociaciones a Promociones",
        notes = "Respuesta: 404 cuando el id no exista.")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Id encontrado. Success."),
        @ApiResponse(code = 404, message = "Id no encontrado.")})
    public ResponseEntity<String> deleteByid (@PathVariable("id") @Valid String id) {
    	if (prendaService.findById(id).isPresent()) {
            prendaService.deleteById(id);
            String msg ="{ \"status\": 200, \"message\": \"Prenda borrada con éxito!\" }";
            JsonObject json = new Gson().fromJson(msg, JsonObject.class);
            return new ResponseEntity<String>(json.toString(), HttpStatus.OK);
    	} else {
            String msg ="{ \"status\": 404, \"message\": \"Elemento no encontrado: " + id + "\" }";
            JsonObject json = new Gson().fromJson(msg, JsonObject.class);
            return new ResponseEntity<String>(json.toString(), HttpStatus.NOT_FOUND);
        }
    }
    
    /* POST /api/v1/prendas
     * El RequestBody acepta un objeto PrendaDto. Aisgnará "prendaId", "precio" y "categorias",
     * y omitirá otros campos como precio promocional o promociones.
    */
    @PostMapping()
    @ApiOperation(
        value = "Añade una nueva prenda a la BD.",
        response = PrendaDto.class,
        notes = "Respuesta: 400 si el body detecta errores de validación(@Valid) o cuando el id ya exista.")
    @ApiResponses(value = {
        @ApiResponse(code = 201, message = "Body correcto. Success."),
        @ApiResponse(code = 400, message = "Estructura del Body no válida o Id duplicado.")})
    public ResponseEntity<?> create (@Valid @RequestBody PrendaDto prendaDto) {
    	if (prendaService.findById(prendaDto.getPrendaId()).isPresent()) {
            String msg ="{ \"status\": 400, \"message\": \"Ya existe una prenda con el identificador: "+ prendaDto.getPrendaId()+"\" }";
            JsonObject json = new Gson().fromJson(msg, JsonObject.class);
            return new ResponseEntity<String>(json.toString(), HttpStatus.BAD_REQUEST);
    	} else {
            Prenda pda = mapper.map(prendaDto, Prenda.class); // de DTO al formato de la Entidad
            prendaService.create(pda);
            PrendaDto pDto = mapper.map(pda, PrendaDto.class); // Y si hubo éxito, proceso inverso Entidad a DTO hacia el Body
            return new ResponseEntity<PrendaDto>(pDto, HttpStatus.CREATED);
    	}
    }
}
