package com.article.service.impl;

import com.article.dao.ArticleDao;
import com.article.model.Articles;
import com.article.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ArticleServiceImpl implements ArticleService {
	
	@Autowired
	private ArticleDao articleDao;

	@Override
	public List<Articles> getArticles() {
		return articleDao.getAllArticles();
	}

	@Override
	public Boolean addArticle(Articles articles) {
		return articleDao.addArticle(articles);
	}

	@Override
	public Articles getArticleByTitle(String title) {
		return articleDao.getArticleByTitle(title);
	}
}
