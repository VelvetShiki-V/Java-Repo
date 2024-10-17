package com.vs.cloud_model.controller;

import cn.hutool.json.JSONUtil;
import com.vs.cloud_common.domain.Result;
import com.vs.cloud_model.domain.ModelDoc;
import com.vs.cloud_model.service.ModelService;
import lombok.RequiredArgsConstructor;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/DSLTest")
@RequiredArgsConstructor
public class DLSController {
    private final RestHighLevelClient client;
    private final ModelService modelService;

    @GetMapping("/query")
    public Result queryModel() throws IOException {
//        return Result.success("simpleQuery服务正常", simpleQuery());
        return Result.success("filterQuery服务正常", filterQuery());
    }

    @GetMapping("/aggr")
    public Result queryAggr() throws IOException {
        return Result.success("aggrQuery服务正常", aggrQuery());
    }

    // 简单分页查询
    private List<ModelDoc> simpleQuery() throws IOException {
        // 模拟分页参数
        int pageNo = 1, pageSize = 5;
        // 创建request对象
        SearchRequest request = new SearchRequest("model");
        // 配置查询条件（查询所有）
        request.source().query(QueryBuilders.matchAllQuery());
        // 分页
        request.source().from((pageNo - 1) * pageSize).size(pageSize);
        // 排序
        request.source().sort("mid", SortOrder.ASC);
        // 发送请求
        SearchResponse response = client.search(request, RequestOptions.DEFAULT);
        // 解析结果
        return responseExtract(response);
    }

    // 复杂查询
    private List<ModelDoc> filterQuery() throws IOException {
        SearchRequest request = new SearchRequest("model");
        request.source().query(QueryBuilders.boolQuery()
                // 过滤筛选
                .must(QueryBuilders.matchQuery("name", "ones"))
                .filter(QueryBuilders.termQuery("owner", "velvetshiki"))
        );
        // 高亮查询
        request.source().highlighter(SearchSourceBuilder
                .highlight()
                .field("name")
                .preTags("<em>")
                .postTags("</em>"));
        SearchResponse response = client.search(request, RequestOptions.DEFAULT);
        return responseExtract(response);
    }

    // 查询结果解析
    private List<ModelDoc> responseExtract(SearchResponse response) {
        // 解析出命中数据总数
        SearchHits searchHits = response.getHits();
        long total_hits = searchHits.getTotalHits().value;
        System.out.println("total hits: " + total_hits);
        // 从hits中取出每条hit
        SearchHit[] hits_arr = searchHits.getHits();
        List<ModelDoc> response_list = new ArrayList<>();
        // 将每条数据存入集合中返回
        for(SearchHit hit: hits_arr) {
            String json = hit.getSourceAsString();
            ModelDoc modelDoc = JSONUtil.toBean(json, ModelDoc.class);
            System.out.println("parse model obj: " + modelDoc);
            // 高亮字段需要从highlight取出
            Map<String, HighlightField> hfs = hit.getHighlightFields();
            if(hfs != null && !hfs.isEmpty()) {
                // 根据高亮字段设置
                modelDoc.setName(replaceHighlight(hfs));
            }
            response_list.add(modelDoc);
        }
        return response_list;
    }

    // 传入高亮字段并返回高亮结果覆盖
    private String replaceHighlight(Map<String, HighlightField> hfs) {
        // 取出高亮字段数组返回
        HighlightField hf = hfs.get("name");
        return hf.getFragments()[0].string();
    }

    // 聚合查询
    private List<String> aggrQuery() throws IOException {
        SearchRequest request = new SearchRequest("model");
        // 聚合条件
        request.source().size(0);
        request.source().aggregation(AggregationBuilders.terms("children").field("children").size(10));
        // 结果解析
        SearchResponse response = client.search(request, RequestOptions.DEFAULT);
        return aggrResponseExtract(response);
    }

    // 聚合结果解析
    private List<String> aggrResponseExtract(SearchResponse response) {
        // 根据聚合名称获取聚合
        Aggregations aggregations = response.getAggregations();
        Terms childTerms = aggregations.get("children");
        // 获取聚合中分好类的buckets
        List<? extends Terms.Bucket> buckets = childTerms.getBuckets();
        List<String> childrenList = new ArrayList<>();
        for(Terms.Bucket bucket: buckets) {
            // 从不同bucket获取字段
            childrenList.add(bucket.getKeyAsString());
            System.out.println("bucket " + bucket.getKeyAsString() + " count: " + bucket.getDocCount());
        }
        return childrenList;
    }
}
