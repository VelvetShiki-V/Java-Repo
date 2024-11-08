package com.vs.article.strategy.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.json.JSONUtil;
import com.vs.article.entity.Article;
import com.vs.article.enums.ArticleEnums;
import com.vs.article.model.dto.ArticleSearchDTO;
import com.vs.article.service.ArticleService;
import com.vs.article.strategy.ArticleSearchStrategy;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service("esSearchStrategyImpl")
@RequiredArgsConstructor
public class EsSearchStrategyImpl implements ArticleSearchStrategy {

    private final RestHighLevelClient esClient;

    private final ArticleService articleService;

    // 根据关键词检索已发布文章列表
    @Override
    public List<ArticleSearchDTO> searchArticles(String keywords) {
        // 查询索引库，若不存在则创建
        if(!indexQuery()) {
            indexCreate();              // 建立索引库
            docCreateOrUpdateBulk();    // 创建文档
        }
        return docQuery(keywords);
    }

    // 创建索引库
    @SneakyThrows
    public void indexCreate() {
        CreateIndexRequest request = new CreateIndexRequest("articles");
        request.source(INDEX_ARTICLE, XContentType.JSON);
        esClient.indices().create(request, RequestOptions.DEFAULT);
    }

    // 查询索引库
    public boolean indexQuery() {
        GetIndexRequest request = new GetIndexRequest("articles");
        try {
            esClient.indices().get(request, RequestOptions.DEFAULT);
        } catch (Exception e) {
            log.info("没有索引库，开始创建");
            return false;
        }
        System.out.println("查询到索引库");
        return true;
    }

    // 删除索引库
    @SneakyThrows
    public void indexDelete() {
        DeleteIndexRequest request = new DeleteIndexRequest();
        esClient.indices().delete(request, RequestOptions.DEFAULT);
    }

    // 批处理更新文档
    @SneakyThrows
    public void docCreateOrUpdateBulk() {
        // 数据库同步所有文章数据，封装为文档模型，同步到es索引库
        List<Article> articles = articleService.lambdaQuery().list();
        List<ArticleSearchDTO> docs = articles.stream().map(article -> BeanUtil
                .copyProperties(article, ArticleSearchDTO.class)).toList();
        log.info("获取到文章searchDTO: {}", docs);
        BulkRequest request = new BulkRequest();
        docs.forEach(doc -> request.add(new IndexRequest("articles")
                .id(doc.getId().toString())
                .source(JSONUtil.toJsonStr(doc), XContentType.JSON)));
        esClient.bulk(request, RequestOptions.DEFAULT);
    }

    // 模糊检索keywords
    @SneakyThrows
    public List<ArticleSearchDTO> docQuery(String keywords) {
        log.info("keywords: {}", keywords);
        SearchRequest request = new SearchRequest("articles");
        request.source().query(QueryBuilders.boolQuery()
                .filter(QueryBuilders.termQuery("isDelete", ArticleEnums.Delete.IS_NOT_DELETED.getValue()))
                .filter(QueryBuilders.termQuery("status", ArticleEnums.Status.PUBLIC.getValue()))
                .must(QueryBuilders.boolQuery()
                        .should(QueryBuilders.matchQuery("articleTitle", keywords))
                        .should(QueryBuilders.matchQuery("articleContent", keywords))
                        .minimumShouldMatch(1) // 至少满足 should 中的一个条件
        ));
        SearchResponse response = esClient.search(request, RequestOptions.DEFAULT);
        return responseExtract(response);
    }

    // 结果提取封装
    private List<ArticleSearchDTO> responseExtract(SearchResponse response) {
        // 解析出命中数据总数
        SearchHits searchHits = response.getHits();
        long total_hits = searchHits.getTotalHits().value;
        log.info("检索出{}条数据", total_hits);
        // 从hits中取出每条hit
        SearchHit[] hits_arr = searchHits.getHits();
        List<ArticleSearchDTO> response_list = new ArrayList<>();
        // 将每条数据存入集合中返回
        for(SearchHit hit: hits_arr) {
            response_list.add(ArticleSearchDTO.builder()
                    .articleTitle(hit.getSourceAsMap().get("articleTitle").toString())
                    .articleContent(hit.getSourceAsMap().get("articleContent").toString())
                    .id((Integer) hit.getSourceAsMap().get("id"))
                    .status((Integer) hit.getSourceAsMap().get("status"))
                    .build()
            );
        }
        return response_list;
    }

    // TODO: 结果关键字高光

    // 文章索引库和文档模型
    private final String INDEX_ARTICLE = "{\n" +
            "  \"mappings\": {\n" +
            "    \"properties\": {\n" +
            "      \"articleTitle\": {\n" +
            "        \"type\": \"text\",\n" +
            "        \"analyzer\": \"ik_smart\"\n" +
            "      },\n" +
            "      \"articleContent\": {\n" +
            "        \"type\": \"text\",\n" +
            "        \"analyzer\": \"ik_smart\"\n" +
            "      },\n" +
            "      \"isDelete\": {\n" +
            "        \"type\": \"keyword\"\n" +
            "      },\n" +
            "      \"status\": {\n" +
            "        \"type\": \"keyword\"\n" +
            "      }\n" +
            "    }\n" +
            "  }\n" +
            "}";
}
