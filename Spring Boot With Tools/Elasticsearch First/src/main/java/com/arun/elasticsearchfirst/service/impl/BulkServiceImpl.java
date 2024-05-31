package com.arun.elasticsearchfirst.service.impl;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.query_dsl.MatchAllQuery;
import co.elastic.clients.elasticsearch.core.BulkRequest;
import co.elastic.clients.elasticsearch.core.MgetRequest;
import co.elastic.clients.elasticsearch.core.MgetResponse;
import co.elastic.clients.elasticsearch.core.MsearchRequest;
import co.elastic.clients.elasticsearch.core.MsearchResponse;
import co.elastic.clients.elasticsearch.core.mget.MultiGetOperation;
import co.elastic.clients.elasticsearch.core.msearch.MultisearchBody;
import co.elastic.clients.elasticsearch.core.msearch.MultisearchHeader;
import co.elastic.clients.elasticsearch.core.msearch.RequestItem;
import com.arun.elasticsearchfirst.entitys.Product;
import com.arun.elasticsearchfirst.service.BulkService;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@Service
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BulkServiceImpl implements BulkService {

    @Autowired ElasticsearchClient elasticsearchClient;
    public static final String PRODUCT_INDEX = "product";

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
    public Object bulkRead() throws IOException {
        MgetRequest mgetRequest = MgetRequest.of(x -> x
                .ids("1", "2", "3", "4", "101")
                .index(PRODUCT_INDEX));

        return elasticsearchClient.mget(mgetRequest, Product.class).docs()
                .stream().filter(x -> x.result().found()).map(y -> y.result().source()).toList();
    }

    @Override
    public Object bulkSearch() throws IOException {
        var series = elasticsearchClient.msearch(MsearchRequest.of(ms -> ms.searches(
                        List.of(RequestItem.of(ri -> ri
                                        .header(MultisearchHeader.of(mh -> mh.index(PRODUCT_INDEX)))
                                        .body(MultisearchBody.of(msb -> msb.query(MatchAllQuery.of(ma -> ma)._toQuery()))
                                        )),
                                RequestItem.of(ri -> ri
                                        .header(MultisearchHeader.of(mh -> mh.index("kibana_sample_data_ecommerce")))
                                        .body(MultisearchBody.of(msb -> msb.query(MatchAllQuery.of(ma -> ma)._toQuery()))
                                        )))
                )),
                Map.class);
        return series.responses().stream().flatMap(x -> x.result().hits().hits().stream().map(y -> y.source())).toList();
    }

}
