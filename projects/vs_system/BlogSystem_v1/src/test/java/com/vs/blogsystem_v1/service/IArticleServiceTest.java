package com.vs.blogsystem_v1.service;
import com.baomidou.mybatisplus.extension.toolkit.Db;
import com.vs.blogsystem_v1.mapper.ArticleMapper;
import com.vs.pojo.Article;
import com.vs.pojo.ArticleStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.time.LocalDateTime;
import java.util.List;
import com.vs.utils.TimeFormatUtil;

@SpringBootTest
class IArticleServiceTest {
    @Autowired
    private ArticleService articleService;

//    @Autowired
//    private ArticleMapper articleMapper;

    @Test
    public void testInsert() {
        articleService.save(new Article(null, 5, "K1", "win!!",
        "hihihi", "www.BBC.cn", "www.vs.cn/articles",
        TimeFormatUtil.timeFormat(LocalDateTime.now()) , TimeFormatUtil.timeFormat(LocalDateTime.now()), 22, 44, ArticleStatus.PUBLISHED));
    }

    @Test
    public void find() {
        // like是关键字！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！
//        QueryWrapper<Article> wrapper = new QueryWrapper<Article>().eq("uid", 1);
//        articleMapper.selectList(wrapper);

        // 筛选出仅published的文章
        List<Article> list = Db.lambdaQuery(Article.class).eq(Article::getStatus, ArticleStatus.PUBLISHED).list();
        list.forEach(System.out::println);

        // 枚举值test
//        for (ArticleStatus s: ArticleStatus.values()) {
//            System.out.println(s.getValue());
//            System.out.println(s.getDesc());
//        }
    }
}