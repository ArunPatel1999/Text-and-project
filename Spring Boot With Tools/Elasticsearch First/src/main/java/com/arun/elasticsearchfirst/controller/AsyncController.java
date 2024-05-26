package com.arun.elasticsearchfirst.controller;

import co.elastic.clients.elasticsearch.ElasticsearchAsyncClient;
import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.elasticsearch.core.search.HitsMetadata;
import com.arun.elasticsearchfirst.service.impl.ProductCrudServiceImpl;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/async")
public class AsyncController {

    @Autowired
    ElasticsearchAsyncClient elasticsearchAsyncClient;

    @SneakyThrows
    @GetMapping
    public void getData() {
        elasticsearchAsyncClient
                .exists(b -> b.index(ProductCrudServiceImpl.PRODUCT_INDEX).id("1"))
                .whenComplete((response, exception) -> {
                    if (exception != null) {
                        log.error("Failed to index", exception);
                    } else {
                        log.info("Product exists");
                    }
                });

    }

}
