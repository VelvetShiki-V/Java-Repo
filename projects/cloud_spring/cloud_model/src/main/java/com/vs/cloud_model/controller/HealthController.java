package com.vs.cloud_model.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.vs.cloud_common.domain.Result;
import com.vs.cloud_model.domain.Model;
import com.vs.cloud_model.domain.ModelDoc;
import com.vs.cloud_model.service.ModelService;
import lombok.RequiredArgsConstructor;
import org.apache.http.HttpHost;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class HealthController {
    private final RestHighLevelClient client;
    private final ModelService modelService;

    @GetMapping("/health")
    public Result checkHealth() { return Result.success("cloud_module服务正常", null); }

    @GetMapping("/es")
    public Result search() throws IOException {
        // 初始化客户端
//        client = new RestHighLevelClient(RestClient.builder(
//                HttpHost.create("http://127.0.0.1:9200")
//        ));
        // 索引库操作
        testCreateIndex();
//        testGetIndex();
//        testDeleteIndex();
        // 文档操作
//        testCreateDoc();
//        testUpdateDoc();
        testBulkDoc();
//        testGetDoc();
//        if(client != null) client.close();
        return Result.success("cloud_module服务正常", null);
    }

    // 索引库操作
    // 创建索引库
    void testCreateIndex() throws IOException {
        System.out.println("### create ###");
        // 创建request对象
        CreateIndexRequest request = new CreateIndexRequest("model");
        // 将json数据写入
        request.source(INDEX_MODEL, XContentType.JSON);
        // 3. 数据发送
        client.indices().create(request, RequestOptions.DEFAULT);
    }

    // 查询索引库
    void testGetIndex() throws IOException {
        System.out.println("### get ###");
        GetIndexRequest request = new GetIndexRequest("model");
        String ret = client.indices().get(request, RequestOptions.DEFAULT).toString();
        System.out.println("ret: " + ret);
    }

    // 索引库删除
    void testDeleteIndex() throws IOException {
        System.out.println("### delete ###");
        DeleteIndexRequest request = new DeleteIndexRequest("model");
        client.indices().delete(request, RequestOptions.DEFAULT);
    }

    // 文档crud
    // 创建文档
    void testCreateDoc() throws IOException {
        IndexRequest request = new IndexRequest("model").id("1");
        request.source(INDEX_DATA, XContentType.JSON);
        client.index(request, RequestOptions.DEFAULT);
    }

    // 更新文档
    void testUpdateDoc() throws IOException {
        // 局部更新
        UpdateRequest request = new UpdateRequest("model", "1");
        request.doc(
                "author", "velvet",
                "page", "200"
        );
        client.update(request, RequestOptions.DEFAULT);
    }

    // 查询文档
    void testGetDoc() throws IOException {
        GetRequest request = new GetRequest("model", "1836614721325199361");
        GetResponse response = client.get(request, RequestOptions.DEFAULT);
        // 响应结果转json转对象
        String json = response.getSourceAsString();
        ModelDoc modelDoc = JSONUtil.toBean(json, ModelDoc.class);
        System.out.println("get doc: " + modelDoc);
    }

    // 删除文档
    void testDeleteDoc() throws IOException {
        DeleteRequest request = new DeleteRequest("model", "1");
        client.delete(request, RequestOptions.DEFAULT);
    }

    // 文档批处理(同步数据库所有数据到索引库的文档中)
    void testBulkDoc() throws IOException {
        int pageNo = 1, pageSize = 5;
        while(true) {
            System.out.println("cur page: " + pageNo);
            // 数据准备
            Page<Model> page = modelService.lambdaQuery().page(Page.of(pageNo, pageSize));
            List<Model> records = page.getRecords();
//            System.out.println(records);
            // 检查记录是否为空
            if (records == null || records.isEmpty()) {
                System.out.println("检索记录为空，停止分页扫描");
                return;
            }
            // 检查是否还有下一页
            if (pageNo >= page.getPages()) {
                System.out.println("到达最后一页，停止分页扫描");
                return;
            }
            pageNo++;
            BulkRequest request = new BulkRequest();
            // 批文档创建
            for(Model md: records) {
                request.add(
                        new IndexRequest("model")
                                .id(md.getMid())
                                .source(JSONUtil.toJsonStr(BeanUtil.copyProperties(md, ModelDoc.class)), XContentType.JSON));
            }
            // 发送请求
            client.bulk(request, RequestOptions.DEFAULT);
        }
    }

    private final String INDEX_ARTICLE = "{\n" +
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

    private final String INDEX_MODEL = "{\n" +
            "  \"mappings\": {\n" +
            "    \"properties\": {\n" +
            "      \"mid\": {\n" +
            "        \"type\": \"keyword\"\n" +
            "      },\n" +
            "      \"name\": {\n" +
            "        \"type\": \"text\",\n" +
            "        \"analyzer\": \"ik_smart\"\n" +
            "      },\n" +
            "      \"owner\": {\n" +
            "        \"type\": \"text\",\n" +
            "        \"analyzer\": \"ik_smart\"\n" +
            "      },\n" +
            "      \"labels\": {\n" +
            "        \"type\": \"text\",\n" +
            "        \"analyzer\": \"ik_smart\"\n" +
            "      },\n" +
            "      \"children\": {\n" +
            "        \"type\": \"keyword\"\n" +
            "      }\n" +
            "    }\n" +
            "  }\n" +
            "}";

    private final String INDEX_DATA = "{\n" +
            "  \"title\": \"Elon Musk\",\n" +
            "  \"author\": \"Mike Maguare\",\n" +
            "  \"price\": \"24.7\",\n" +
            "  \"page\": \"350\",\n" +
            "  \"description\": \"a book about founder of Tesla and spaceX\",\n" +
            "  \"addition\": {\n" +
            "    \"ISBN\": \"SN-123-456-7890\",\n" +
            "    \"price\": \"30\",\n" +
            "    \"publisher\": \"the New Yoker\"\n" +
            "  }\n" +
            "}";
}
