package com.arun.elasticsearchfirst.service;

import com.arun.elasticsearchfirst.entitys.Product;

import java.io.IOException;
import java.util.List;

public interface BulkService {

    public Object addMultipleProduct(List<Product> products) throws IOException;

    public Object bulkRead() throws  IOException;

    public Object bulkSearch() throws IOException;

}
