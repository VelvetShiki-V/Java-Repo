package com.vs.cloud_model.controller;

import com.vs.cloud_common.domain.Result;
import lombok.RequiredArgsConstructor;
import org.apache.http.HttpHost;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class HealthController {
    private RestHighLevelClient client;

    @GetMapping("/health")
    public Result checkHealth() { return Result.success("cloud_module服务正常", null); }

    @GetMapping("/es")
    public Result search() throws IOException {
        // 初始化客户端
        client = new RestHighLevelClient(RestClient.builder(
                HttpHost.create("http://127.0.0.1:9200")
        ));
        // 销毁客户端
        testCreate();
        testGet();
//        testDelete();
        if(client != null) client.close();
        return Result.success("cloud_module服务正常", null);
    }

    // es api
    void testCreate() throws IOException {
        System.out.println("### create ###");
        CreateIndexRequest request = new CreateIndexRequest("model");
        request.source(MODEL_DATA, XContentType.JSON);
        client.indices().create(request, RequestOptions.DEFAULT);
    }

    void testGet() throws IOException {
        System.out.println("### get ###");
        GetIndexRequest request = new GetIndexRequest("model");
        String ret = client.indices().get(request, RequestOptions.DEFAULT).toString();
        System.out.println("ret: " + ret);
    }

    void testUpdate() {

    }

    void testDelete() throws IOException {
        System.out.println("### delete ###");
        DeleteIndexRequest request = new DeleteIndexRequest("model");
        client.indices().delete(request, RequestOptions.DEFAULT);
    }

    private final String MODEL_DATA = "{\n" +
            "  \"mappings\": {\n" +
            "    \"properties\": {\n" +
            "      \"title\": {\n" +
            "        \"type\": \"text\",\n" +
            "        \"analyzer\": \"ik_smart\"\n" +
            "      },\n" +
            "      \"author\": {\n" +
            "        \"type\": \"keyword\"\n" +
            "      },\n" +
            "      \"page\": {\n" +
            "        \"type\": \"integer\"\n" +
            "      },\n" +
            "      \"description\": {\n" +
            "        \"type\": \"text\",\n" +
            "        \"analyzer\": \"ik_smart\"\n" +
            "      },\n" +
            "      \"addition\": {\n" +
            "        \"properties\": {\n" +
            "          \"ISBN\": {\n" +
            "            \"type\": \"keyword\"\n" +
            "          },\n" +
            "          \"publisher\": {\n" +
            "            \"type\": \"text\"\n" +
            "          },\n" +
            "          \"price\": {\n" +
            "            \"type\": \"integer\"\n" +
            "          }\n" +
            "        }\n" +
            "      }\n" +
            "    }\n" +
            "  }\n" +
            "}";
}
