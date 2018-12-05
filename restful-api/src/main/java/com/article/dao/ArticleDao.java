package com.article.dao;

import com.article.model.Articles;
import java.util.List;

public interface ArticleDao {

	List<Articles> getAllArticles();
	Boolean addArticle(Articles article);
	Articles getArticleByTitle(String title);
}
