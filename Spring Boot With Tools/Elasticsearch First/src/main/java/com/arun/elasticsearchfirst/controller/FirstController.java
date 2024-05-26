package com.arun.elasticsearchfirst.controller;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.elasticsearch.core.search.HitsMetadata;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/first")
public class FirstController {

    @Autowired
    ElasticsearchClient elasticsearchClient;

    @SneakyThrows
    @GetMapping
    public Object getData() {
        HitsMetadata<Map> hits = elasticsearchClient.search(s -> s
                        .index("test")
                        .query(q -> q
                                .match(t -> t
                                        .field("currency")
                                        .query("EUR")
                                )
                        ),
                Map.class
        ).hits();
        return hits.hits().stream().map(Hit::source).toList();
    }

}
