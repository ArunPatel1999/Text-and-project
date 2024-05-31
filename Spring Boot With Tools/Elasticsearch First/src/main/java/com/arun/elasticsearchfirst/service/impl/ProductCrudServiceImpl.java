package com.arun.elasticsearchfirst.service.impl;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.aggregations.Aggregate;
import co.elastic.clients.elasticsearch._types.aggregations.StringTermsBucket;
import co.elastic.clients.elasticsearch.core.BulkRequest;
import co.elastic.clients.elasticsearch.core.GetResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import com.arun.elasticsearchfirst.entitys.Product;
import com.arun.elasticsearchfirst.service.ProductCrudService;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductCrudServiceImpl implements ProductCrudService {

    @Autowired ElasticsearchClient elasticsearchClient;
    public static final String PRODUCT_INDEX = "product";

    @Override
    public Object createIndex() throws IOException {
        System.out.println(elasticsearchClient.indices().create(x -> x.index(PRODUCT_INDEX)));
        return "Index Created Succesfull";
    }

    @Override
    public List<Product> findAll() throws IOException {
        return elasticsearchClient.search(s -> s
                        .index(PRODUCT_INDEX)
                        .query(q -> q.matchAll(ma -> ma.boost(1.0f))),
                Product.class
        ).hits().hits().stream().map(Hit::source).toList();
    }

    @Override
    public Product findById(Long id) throws Exception {
        GetResponse<Product> productGetResponse = elasticsearchClient.get(x -> x.index(PRODUCT_INDEX).id(id.toString()), Product.class);
        if(productGetResponse.found())
            return productGetResponse.source();
        else
            throw new Exception("Not Found.");
    }

    @Override
    public Object addProduct(Product product) throws IOException {
        return  elasticsearchClient.index(i -> i
                .index(PRODUCT_INDEX)
                .id(product.getId().toString())
                .document(product)
        );
    }

    @Override
    public Object addMultipleProduct(List<Product> products) throws IOException {
        BulkRequest.Builder br = new BulkRequest.Builder();
        for (Product product : products) {
            br.operations(op -> op
                    .index(idx -> idx
                            .index(PRODUCT_INDEX)
                            .id(product.getId().toString())
                            .document(product)
                    )
            );
        }

        return elasticsearchClient.bulk(br.build());
    }

    @Override
    public Object deleteById(Long id) throws  IOException {
        return elasticsearchClient.delete(i -> i.index(PRODUCT_INDEX).id(id.toString()));
    }

    @Override
    public  Object deleteIndex() throws  IOException {
        return elasticsearchClient.indices().delete(d -> d.index(PRODUCT_INDEX));
    }

    @Override
    public Object continentAndCitiesAggrgation() throws IOException {
        Map<String, Aggregate> aggregations = elasticsearchClient.search(s -> s
                        .index("kibana_sample_data_ecommerce")
                        .size(0)
                        .aggregations("Contains", continent ->
                                        continent.terms(t -> t.field("geoip.continent_name").size(10))
                                        .aggregations("Cities", cities -> cities.terms(
                                                t -> t.field("geoip.city_name").size(10)
                                        ))
                        ),
                Void.class
        ).aggregations();
        List<Map<String, Object>> result = new LinkedList<>();

        for (StringTermsBucket continentBucket : aggregations.get("Contains").sterms().buckets().array()) {
            Map<String, Object> continentMap = new HashMap<>();
            continentMap.put("key", continentBucket.key().stringValue());
            continentMap.put("doc_count", continentBucket.docCount());

            List<Map<String, Object>> cityBuckets = new LinkedList<>();
            for (StringTermsBucket cityBucket : continentBucket.aggregations().get("Cities").sterms().buckets().array()) {
                Map<String, Object> cityMap = new HashMap<>();
                cityMap.put("key", cityBucket.key().stringValue());
                cityMap.put("doc_count", cityBucket.docCount());
                cityBuckets.add(cityMap);
            }
            continentMap.put("cities", cityBuckets);
            result.add(continentMap);
        }
        return result;
    }

}
