package com.acme.jfc;

import com.acme.jfc.dto.PrendaDto;
import com.acme.jfc.dto.PrendaPromocionadaDto;
import com.acme.jfc.dto.PromocionDto;
import com.acme.jfc.dto.PromocionRebajasDto;
import com.acme.jfc.entity.Prenda;

import com.google.gson.JsonObject;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

@TestMethodOrder(OrderAnnotation.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ApiRestApplicationTests {
   
    @LocalServerPort
    private int port;
    
    @Autowired
    private TestRestTemplate restTemplate;
    
    private static final Logger logger = LoggerFactory.getLogger(ApiRestApplicationTests.class);
    private static String host ="http://localhost:";
    private static String route ="/api/v1";

    @Test
    @Order(1) 
    void contextLoads() {
    }
    
    @Test
    @Order(2) 
    void testPrendasGetAll() {
        String url = host + port + route + "/prendas";
        ResponseEntity<?> response = restTemplate.exchange(
                url, HttpMethod.GET, HttpEntity.EMPTY, PrendaDto[].class);
        Assertions.assertEquals(response.getStatusCode(), HttpStatus.OK);
        Assertions.assertEquals(response.getHeaders().getContentType(), MediaType.APPLICATION_JSON);
        Assertions.assertNotNull(response.getBody());
        PrendaDto[] pdas = (PrendaDto[]) response.getBody();
        Assertions.assertNotNull(pdas);
    }
    
    @Test
    @Order(3)
    void testPrendasGetAllFull() {
        String url = host + port + route + "/prendas?full=true";
        ResponseEntity<?> response = restTemplate.exchange(
                url, HttpMethod.GET, HttpEntity.EMPTY, PrendaPromocionadaDto[].class);
        Assertions.assertEquals(response.getStatusCode(), HttpStatus.OK);
        Assertions.assertEquals(response.getHeaders().getContentType(), MediaType.APPLICATION_JSON);
        Assertions.assertNotNull(response.getBody());
        PrendaPromocionadaDto[] pdas = (PrendaPromocionadaDto[]) response.getBody();
        Assertions.assertNotNull(pdas);
    }
    
    @Test
    @Order(4)
    void testPrendasGetById() {
        String id = "S123456789";
        String url = host + port + route + "/prendas/" + id;
        ResponseEntity<?> response = restTemplate.exchange(
                url, HttpMethod.GET, HttpEntity.EMPTY, PrendaPromocionadaDto.class);
        Assertions.assertEquals(response.getStatusCode(), HttpStatus.OK);
        Assertions.assertEquals(response.getHeaders().getContentType(), MediaType.APPLICATION_JSON);
        Assertions.assertNotNull(response.getBody());
        PrendaPromocionadaDto p = (PrendaPromocionadaDto) response.getBody();
        Assertions.assertNotNull(p);
        Assertions.assertTrue(p.getPrendaId().contains("S123456789"));
    }
    
    @Test
    @Order(5)
    void testPromocionesGetAll() {
        String url = host + port + route + "/promociones";
        ResponseEntity<?> response = restTemplate.exchange(
                url, HttpMethod.GET, HttpEntity.EMPTY, PromocionDto[].class);
        Assertions.assertEquals(response.getStatusCode(), HttpStatus.OK);
        Assertions.assertEquals(response.getHeaders().getContentType(), MediaType.APPLICATION_JSON);
        Assertions.assertNotNull(response.getBody());
        PromocionDto[] ps = (PromocionDto[]) response.getBody();
        Assertions.assertNotNull(ps);
    }
    
    @Test
    @Order(6)
    void testPromocionesGetAllFull() {
        String url = host + port + route + "/promociones?full=true";
        ResponseEntity<?> response = restTemplate.exchange(
                url, HttpMethod.GET, HttpEntity.EMPTY, PromocionRebajasDto[].class);
        Assertions.assertEquals(response.getStatusCode(), HttpStatus.OK);
        Assertions.assertEquals(response.getHeaders().getContentType(), MediaType.APPLICATION_JSON);
        Assertions.assertNotNull(response.getBody());
        PromocionRebajasDto[] ps = (PromocionRebajasDto[]) response.getBody();
        Assertions.assertNotNull(ps);
    }
    
    @Test
    @Order(7)
    void testPromocionesGetAllSearch() {
        String url = host + port + route + "/promociones?search=BF";
        ResponseEntity<?> response = restTemplate.exchange(
                url, HttpMethod.GET, HttpEntity.EMPTY, PromocionRebajasDto[].class);
        Assertions.assertEquals(response.getStatusCode(), HttpStatus.OK);
        Assertions.assertEquals(response.getHeaders().getContentType(), MediaType.APPLICATION_JSON);
        Assertions.assertNotNull(response.getBody());
        PromocionRebajasDto[] ps = (PromocionRebajasDto[]) response.getBody();
        Assertions.assertNotNull(ps);
    }
    
    @Test
    @Order(8)
    void testPromocionesGetById() {
        String id = "BF2022";
        String url = host + port + route + "/promociones/" + id;
        ResponseEntity<?> response = restTemplate.exchange(
                url, HttpMethod.GET, HttpEntity.EMPTY, PromocionRebajasDto.class);
        Assertions.assertEquals(response.getStatusCode(), HttpStatus.OK);
        Assertions.assertEquals(response.getHeaders().getContentType(), MediaType.APPLICATION_JSON);
        Assertions.assertNotNull(response.getBody());
        PromocionRebajasDto p = (PromocionRebajasDto) response.getBody();
        Assertions.assertNotNull(p);
        Assertions.assertTrue(p.getPromocionId().contains("BF2022"));
    }    

    
    @Test
    @Order(9)
    void testPrendasUpdate() {
        String id = "S123456789";
        String url = host + port + route + "/prendas/"+id;
        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);
        headers.add("Content-Type", MediaType.APPLICATION_JSON_VALUE);
        Prenda pNew = Prenda.builder()
                .precio(19.99)
                .build();
        HttpEntity<?> entity = new HttpEntity(pNew,headers);
        ResponseEntity<?> response = restTemplate.exchange(
                url, HttpMethod.PUT, entity, PrendaDto.class);
        Assertions.assertEquals(response.getStatusCode(), HttpStatus.OK);
        Assertions.assertEquals(response.getHeaders().getContentType(), MediaType.APPLICATION_JSON);
        Assertions.assertNotNull(response.getBody());
        PrendaDto pDto = (PrendaDto) response.getBody();
        Assertions.assertNotNull(pDto);
    }

    @Test
    @Order(10)
    void testPromocionesUpdate() {
        String id = "REBAJAS";
        String url = host + port + route + "/promociones/"+id;
        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);
        headers.add("Content-Type", MediaType.APPLICATION_JSON_VALUE);
        PromocionDto pNew = new PromocionDto();
        pNew.setPromocionId(id);
        pNew.setNombre("Las Rebajas");
        HttpEntity<?> entity = new HttpEntity(pNew, headers);
        ResponseEntity<?> response = restTemplate.exchange(
                url, HttpMethod.PUT, entity, PromocionDto.class);
        Assertions.assertEquals(response.getStatusCode(), HttpStatus.OK);
        Assertions.assertEquals(response.getHeaders().getContentType(), MediaType.APPLICATION_JSON);
        Assertions.assertNotNull(response.getBody());
        PromocionDto pDto = (PromocionDto) response.getBody();
        Assertions.assertNotNull(pDto);
    }
    
    @Test
    @Order(11)
    void testPromocionesPutPrendas() {
        String id = "REBAJAS";
        String url = host + port + route + "/promociones/" + id + "/prendas";
        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);
        headers.add("Content-Type", MediaType.APPLICATION_JSON_VALUE);
        List<String> listado = new ArrayList<>();
        listado.add("S123456789");
        listado.add("S987654321");
        HttpEntity<?> entity = new HttpEntity(listado, headers);
        ResponseEntity<?> response = restTemplate.exchange(
                url, HttpMethod.PUT, entity, String.class);
        Assertions.assertEquals(response.getStatusCode(), HttpStatus.OK);
        Assertions.assertEquals(response.getHeaders().getContentType(), MediaType.APPLICATION_JSON);
        Assertions.assertNotNull(response.getBody());
    }    

    @Test
    @Order(12)
    void testPromocionesDeletePrendas() {
        String id = "REBAJAS";
        String url = host + port + route + "/promociones/" + id + "/prendas";
        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);
        headers.add("Content-Type", MediaType.APPLICATION_JSON_VALUE);
        List<String> listado = new ArrayList<>();
        listado.add("S123456789");
        listado.add("S987654321");
        HttpEntity<?> entity = new HttpEntity(listado, headers);
        ResponseEntity<?> response = restTemplate.exchange(
                url, HttpMethod.DELETE, entity, String.class);
        Assertions.assertEquals(response.getStatusCode(), HttpStatus.OK);
        Assertions.assertEquals(response.getHeaders().getContentType(), MediaType.APPLICATION_JSON);
        Assertions.assertNotNull(response.getBody());
    }
    
    @Test
    @Order(13)
    void testPrendasCreate() {
        String url = host + port + route + "/prendas";
        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);
        headers.add("Content-Type", MediaType.APPLICATION_JSON_VALUE);
        PrendaDto pNew = new PrendaDto();
        pNew.setPrendaId("L123123123");
        pNew.setPrecio(48.90);
        HttpEntity<?> entity = new HttpEntity(pNew, headers); 
        ResponseEntity<?> response = restTemplate.exchange(
                url, HttpMethod.POST, entity, PrendaDto.class);
        logger.info(response.toString());
        Assertions.assertEquals(response.getStatusCode(), HttpStatus.CREATED);
        Assertions.assertEquals(response.getHeaders().getContentType(), MediaType.APPLICATION_JSON);
        Assertions.assertNotNull(response.getBody());
        PrendaDto pDto = (PrendaDto) response.getBody();
        Assertions.assertNotNull(pDto);
        Assertions.assertNotNull(pDto.getPrendaId());
    }    

    @Test
    @Order(14)
    void testPrendasDeleteById() {
        String id = "L123123123";
        String url = host + port + route + "/prendas/" + id;
        ResponseEntity<?> response = restTemplate.exchange(
                url, HttpMethod.DELETE, HttpEntity.EMPTY, JsonObject.class);
        Assertions.assertEquals(response.getStatusCode(), HttpStatus.OK);
        Assertions.assertEquals(response.getHeaders().getContentType(), MediaType.APPLICATION_JSON);
        Assertions.assertNotNull(response.getBody());
        JsonObject p = (JsonObject) response.getBody();
        Assertions.assertNotNull(p);
    }
    
    @Test
    @Order(15)
    void testPromocionesCreate() {
        String url = host + port + route + "/promociones";
        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);
        headers.add("Content-Type", MediaType.APPLICATION_JSON_VALUE);
        PromocionDto pNew = new PromocionDto();
        pNew.setPromocionId("OTRO");
        pNew.setDescuento(8.90);
        HttpEntity<?> entity = new HttpEntity(pNew, headers); 
        ResponseEntity<?> response = restTemplate.exchange(
                url, HttpMethod.POST, entity, PromocionDto.class);
        logger.info(response.toString());
        Assertions.assertEquals(response.getStatusCode(), HttpStatus.CREATED);
        Assertions.assertEquals(response.getHeaders().getContentType(), MediaType.APPLICATION_JSON);
        Assertions.assertNotNull(response.getBody());
        PromocionDto pDto = (PromocionDto) response.getBody();
        Assertions.assertNotNull(pDto);
        Assertions.assertNotNull(pDto.getNombre());
    }    
    
    @Test
    @Order(16)
    void testPromocionesDeleteById() {
        String id = "REBAJAS";
        String url = host + port + route + "/promociones/" + id;
        ResponseEntity<?> response = restTemplate.exchange(
                url, HttpMethod.DELETE, HttpEntity.EMPTY, JsonObject.class);
        Assertions.assertEquals(response.getStatusCode(), HttpStatus.OK);
        Assertions.assertEquals(response.getHeaders().getContentType(), MediaType.APPLICATION_JSON);
        Assertions.assertNotNull(response.getBody());
        JsonObject p = (JsonObject) response.getBody();
        Assertions.assertNotNull(p);
    }
    
}
