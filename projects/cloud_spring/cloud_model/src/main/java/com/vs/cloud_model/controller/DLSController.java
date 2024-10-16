package com.vs.cloud_model.controller;

import com.vs.cloud_common.domain.Result;
import com.vs.cloud_model.service.ModelService;
import lombok.RequiredArgsConstructor;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/DLS")
@RequiredArgsConstructor
public class DLSController {
    private final RestHighLevelClient client;
    private final ModelService modelService;

    @GetMapping("/query")
    public Result queryModel() throws IOException {
//        return Result.success("simpleQuery服务正常", simpleQuery());
        return Result.success("simpleQuery服务正常", filterQuery());
    }

    // 简单查询
    private List<String> simpleQuery() throws IOException {
        // 创建request对象
        SearchRequest request = new SearchRequest("model");
        // 配置查询条件
        request.source().query(QueryBuilders.matchAllQuery());
        // 发送请求
        SearchResponse response = client.search(request, RequestOptions.DEFAULT);
        // 解析结果
        return responseExtract(response);
    }

    // 复杂查询
    private List<String> filterQuery() throws IOException {
        SearchRequest request = new SearchRequest("model");
        request.source().query(QueryBuilders.boolQuery()
                .must(QueryBuilders.matchQuery("name", "ones"))
                .filter(QueryBuilders.termQuery("owner", "velvetshiki"))
        );
        SearchResponse response = client.search(request, RequestOptions.DEFAULT);
        return responseExtract(response);
    }

    private List<String> responseExtract(SearchResponse response) {
        // 解析出命中数据总数
        SearchHits searchHits = response.getHits();
        long total_hits = searchHits.getTotalHits().value;
        System.out.println("total hits: " + total_hits);
        // 从hits中取出每条hit
        SearchHit[] hits_arr = searchHits.getHits();
        List<String> response_list = new ArrayList<>();
        // 将每条数据存入集合中返回
        for(SearchHit hit: hits_arr) {
            System.out.println("parse arr: " + hit.getSourceAsString());
            response_list.add(hit.getSourceAsString());
        }
        return response_list;
    }
}
