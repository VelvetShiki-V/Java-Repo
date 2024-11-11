package com.vs.article.constant;

public interface ArticleConstant {
    // 文章索引库和文档模型

    String INDEX_NAME = "articles";

    String INDEX_ARTICLE = "{\n" +
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
