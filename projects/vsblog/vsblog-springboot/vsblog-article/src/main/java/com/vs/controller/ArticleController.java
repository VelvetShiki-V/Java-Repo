package com.vs.controller;

import com.vs.entity.Article;
import com.vs.service.ArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("article")
@RequiredArgsConstructor
public class ArticleController {

    private final ArticleService articleService;

    @GetMapping
    public ResponseEntity<Page<Article>> queryByPage(Article article, PageRequest pageRequest) {
        return ResponseEntity.ok(this.articleService.queryByPage(article, pageRequest));
    }

    @GetMapping("{id}")
    public ResponseEntity<Article> queryById(@PathVariable("id") Integer id) {
        return ResponseEntity.ok(this.articleService.queryById(id));
    }

    @PostMapping
    public ResponseEntity<Article> add(Article article) {
        return ResponseEntity.ok(this.articleService.insert(article));
    }

    @PutMapping
    public ResponseEntity<Article> edit(Article article) {
        return ResponseEntity.ok(this.articleService.update(article));
    }

    @DeleteMapping
    public ResponseEntity<Boolean> deleteById(Integer id) {
        return ResponseEntity.ok(this.articleService.deleteById(id));
    }

}

