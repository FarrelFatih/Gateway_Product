package com.multipolar.bootcamp.gatewayProduct.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.multipolar.bootcamp.gatewayProduct.dto.ErrorMessageDTO;
import com.multipolar.bootcamp.gatewayProduct.dto.ProductDTO;
import com.multipolar.bootcamp.gatewayProduct.kafka.AccessLog;
import com.multipolar.bootcamp.gatewayProduct.service.AccessLogService;
import com.multipolar.bootcamp.gatewayProduct.util.RestTemplateUtil;

@RestController
@RequestMapping("/api")
public class ApiController {

    private static final String BANKPRODUCT_URL = "http://localhost:8083/bankProduct";
    private final RestTemplateUtil restTemplateUtil;
    private final ObjectMapper objectMapper;
    private final AccessLogService accessLogService;

    @Autowired
    public ApiController(RestTemplateUtil restTemplateUtil, ObjectMapper objectMapper,
            AccessLogService accessLogService) {
        this.restTemplateUtil = restTemplateUtil;
        this.objectMapper = objectMapper;
        this.accessLogService = accessLogService;
    }

    @GetMapping("/getProducts")
    public ResponseEntity<?> getCustomers() throws JsonProcessingException {
        // akses API Customer dan dapatkan datanya
        try {
            ResponseEntity<?> response = restTemplateUtil.getList(BANKPRODUCT_URL, new ParameterizedTypeReference<>() {
            });
            AccessLog accessLog = new AccessLog("GET", BANKPRODUCT_URL, response.getStatusCodeValue(),
                    LocalDateTime.now(),
                    "Successful");
            accessLogService.logAccess(accessLog);
            return ResponseEntity.status(response.getStatusCode()).body(response.getBody());
        } catch (HttpClientErrorException ex) {
            List<ErrorMessageDTO> errorResponse = objectMapper.readValue(ex.getResponseBodyAsString(), List.class);
            AccessLog accessLog = new AccessLog("GET", BANKPRODUCT_URL, ex.getRawStatusCode(), LocalDateTime.now(),
                    "failed");
            accessLogService.logAccess(accessLog);
            return ResponseEntity.status(ex.getStatusCode()).body(errorResponse);
        }
    }

    @GetMapping("/getProducts/{id}")
    public ResponseEntity<?> getCustomersById(@PathVariable String id) throws JsonProcessingException {
        // akses API Customer dan dapatkan datanya
        try {
            ResponseEntity<?> response = restTemplateUtil.getList(BANKPRODUCT_URL + "/" + id,
                    new ParameterizedTypeReference<>() {
                    });
            // Kirim AccessLog ke kafka
            AccessLog accessLog = new AccessLog("GET by ID", BANKPRODUCT_URL, response.getStatusCodeValue(),
                    LocalDateTime.now(),
                    "Successful");
            accessLogService.logAccess(accessLog);
            return ResponseEntity.status(response.getStatusCode()).body(response.getBody());
        } catch (HttpClientErrorException ex) {
            List<ErrorMessageDTO> errorResponse = objectMapper.readValue(ex.getResponseBodyAsString(), List.class);
            AccessLog accessLog = new AccessLog("GET by ID", BANKPRODUCT_URL, ex.getRawStatusCode(),
                    LocalDateTime.now(),
                    "failed");
            accessLogService.logAccess(accessLog);
            return ResponseEntity.status(ex.getStatusCode()).body(errorResponse);
        }
    }

    @PostMapping("/createProducts")
    public ResponseEntity<?> postCustomers(@RequestBody ProductDTO productDTO) throws JsonProcessingException {
        // akses API Customer dan buat data baru
        try {
            ResponseEntity<?> response = restTemplateUtil.post(BANKPRODUCT_URL, productDTO, ProductDTO.class);
            AccessLog accessLog = new AccessLog("POST", BANKPRODUCT_URL, response.getStatusCodeValue(),
                    LocalDateTime.now(),
                    "Successful");
            accessLogService.logAccess(accessLog);
            return ResponseEntity.status(response.getStatusCode()).body(response.getBody());
        } catch (HttpClientErrorException ex) {
            List<ErrorMessageDTO> errorResponse = objectMapper.readValue(ex.getResponseBodyAsString(), List.class);
            AccessLog accessLog = new AccessLog("POST", BANKPRODUCT_URL, ex.getRawStatusCode(), LocalDateTime.now(),
                    "failed");
            accessLogService.logAccess(accessLog);
            return ResponseEntity.status(ex.getStatusCode()).body(errorResponse);
        }
    }

    @PutMapping("/updateProducts/{id}")
    public ResponseEntity<?> updateCustomers(@RequestBody ProductDTO productDTO, @PathVariable String id)
            throws JsonProcessingException {
        // akses API Customer dan update data
        try {
            ResponseEntity<?> response = restTemplateUtil.put(BANKPRODUCT_URL + "/" + id, productDTO, ProductDTO.class);
            AccessLog accessLog = new AccessLog("UPDATE", BANKPRODUCT_URL, response.getStatusCodeValue(),
                    LocalDateTime.now(), "Successful");
            accessLogService.logAccess(accessLog);
            return ResponseEntity.status(response.getStatusCode()).body(response.getBody());
        } catch (HttpClientErrorException ex) {
            List<ErrorMessageDTO> errorResponse = objectMapper.readValue(ex.getResponseBodyAsString(), List.class);
            AccessLog accessLog = new AccessLog("UPDATE", BANKPRODUCT_URL, ex.getRawStatusCode(), LocalDateTime.now(),
                    "failed");
            accessLogService.logAccess(accessLog);
            return ResponseEntity.status(ex.getStatusCode()).body(errorResponse);
        }
    }

    @DeleteMapping("/deleteProducts/{id}")
    public ResponseEntity<?> deleteCustomers(@PathVariable String id)
            throws JsonProcessingException {
        // akses API Customer dan hapus data
        try {
            restTemplateUtil.delete(BANKPRODUCT_URL + "/" + id);
            AccessLog accessLog = new AccessLog("DELETE", BANKPRODUCT_URL, HttpStatus.OK.value(),
                    LocalDateTime.now(), "Successful");
            accessLogService.logAccess(accessLog);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (HttpClientErrorException ex) {
            List<ErrorMessageDTO> errorResponse = objectMapper.readValue(ex.getResponseBodyAsString(), List.class);
            AccessLog accessLog = new AccessLog("DELETE", BANKPRODUCT_URL, ex.getRawStatusCode(), LocalDateTime.now(),
                    "failed");
            accessLogService.logAccess(accessLog);
            return ResponseEntity.status(ex.getStatusCode()).body(errorResponse);
        }
    }
}
