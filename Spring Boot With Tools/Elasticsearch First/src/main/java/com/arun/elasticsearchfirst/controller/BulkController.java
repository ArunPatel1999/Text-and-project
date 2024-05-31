package com.arun.elasticsearchfirst.controller;

import com.arun.elasticsearchfirst.entitys.Product;
import com.arun.elasticsearchfirst.service.BulkService;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/bulk")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BulkController {

    @Autowired
    BulkService bulkService;

    @PostMapping("/addMultipleProduct")
    public ResponseEntity<Object> addProduct(@RequestBody List<Product> products) throws IOException {
        return ResponseEntity.ok(bulkService.addMultipleProduct(products));
    }

    @GetMapping("/readMultId")
    public ResponseEntity<Object> readMultId() throws IOException {
        return ResponseEntity.ok(bulkService.bulkRead());
    }

    @GetMapping("/bulkSearch")
    public ResponseEntity<Object> bulkSearch() throws IOException {
        return ResponseEntity.ok(bulkService.bulkSearch());
    }

}
