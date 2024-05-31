package com.arun.elasticsearchfirst.controller;

import com.arun.elasticsearchfirst.entitys.Product;
import com.arun.elasticsearchfirst.service.ProductCrudService;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/product")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductCrudController {

    @Autowired
    ProductCrudService productCrudService;

    @PostMapping("/createIndedx")
    public ResponseEntity<Object> createIndex() throws IOException {
        return ResponseEntity.ok(productCrudService.createIndex());
    }

    @GetMapping("/findAll")
    public ResponseEntity<Object> findAll() throws IOException {
        return ResponseEntity.ok(productCrudService.findAll());
    }

    @GetMapping("/findById")
    public ResponseEntity<Object> findById(@RequestParam Long id) throws Exception {
        return ResponseEntity.ok(productCrudService.findById(id));
    }

    @PostMapping("/addProduct")
    public ResponseEntity<Object> addProduct(@RequestBody Product product) throws IOException {
        return ResponseEntity.ok(productCrudService.addProduct(product));
    }

    @DeleteMapping("/deleteById")
    public ResponseEntity<Object> deleteById(@RequestParam Long id) throws IOException {
        return ResponseEntity.ok(productCrudService.deleteById(id));
    }
    @DeleteMapping("/deleteIndedx")
    public ResponseEntity<Object> deleteIndedx() throws IOException {
        return ResponseEntity.ok(productCrudService.deleteIndex());
    }

    @GetMapping("/continentAndCitiesAggrgation")
    public ResponseEntity<Object> continentAndCitiesAggrgation() throws IOException {
        return ResponseEntity.ok(productCrudService.continentAndCitiesAggrgation());
    }
}
