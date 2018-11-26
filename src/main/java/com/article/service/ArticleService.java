/**
 * 
 */
package com.article.service;

import com.article.model.Articles;

import java.util.List;

public interface ArticleService {

	List<Articles> getArticles();
	Boolean addArticle(Articles articles);
	Articles getArticleByTitle(String title);
}
