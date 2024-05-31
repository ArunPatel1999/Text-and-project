package com.arun.elasticsearchfirst.service;

import co.elastic.clients.elasticsearch.core.GetResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import com.arun.elasticsearchfirst.entitys.Product;

import java.io.IOException;
import java.util.List;

public interface ProductCrudService {

    public Object createIndex() throws IOException;

    public List<Product> findAll() throws IOException;

    public Product findById(Long id) throws Exception ;

    public Object addProduct(Product product) throws IOException;

    public Object addMultipleProduct(List<Product> products) throws IOException;

    public Object deleteById(Long id) throws IOException;

    public  Object deleteIndex() throws  IOException;

    public Object continentAndCitiesAggrgation() throws IOException;
}
