package com.article.dao.impl;

import java.util.List;
import com.article.dao.ArticleDao;
import com.article.model.Articles;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ArticleDaoImpl implements ArticleDao {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public List<Articles> getAllArticles() {
        Criteria criteria = sessionFactory.openSession().createCriteria(Articles.class);
        return criteria.list();
    }

    @Override
    public Boolean addArticle(Articles article) {
        Session session = sessionFactory.openSession();
        Transaction tx = session.beginTransaction();
        session.save(article);
        tx.commit();
        session.close();
        return true;
    }

    @Override
    public Articles getArticleByTitle(String title) {
        Criteria criteria = sessionFactory.openSession().createCriteria(Articles.class);
        Articles articles = (Articles) criteria.add(Restrictions.eq("title", title))
                .uniqueResult();
        return articles;
    }
}
