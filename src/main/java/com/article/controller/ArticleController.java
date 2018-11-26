package com.article.controller;

import com.article.model.Articles;
import com.article.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    @RequestMapping(value = "/com/article/list", method = RequestMethod.GET)
    public ResponseEntity<List<Articles>> articles() {
        List<Articles> articles = articleService.getArticles();
        return new ResponseEntity<List<Articles>>(articles, HttpStatus.OK);
    }

    @RequestMapping(value = "/com/article/add", method = RequestMethod.POST)
    public ResponseEntity < String > addArticle(@RequestBody Articles articles) {
        articleService.addArticle(articles);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @RequestMapping(value = "/com/article/title", method = RequestMethod.GET)
    public ResponseEntity<Articles> getArticlesByTitle(@RequestParam("title") String title) {
        System.out.println(title);

        Articles articles1 = articleService.getArticleByTitle(title);

         System.out.println(articles1.getTitle());
        return new ResponseEntity<Articles>(articles1, HttpStatus.OK);
    }
}

