package com.article.dao.impl;

import com.article.dao.UserDao;
import com.article.model.UserDetails;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserDaoImpl implements UserDao {

	@Autowired
	private SessionFactory sessionFactory;
	
	public List<UserDetails> getUserDetails() {
		Criteria criteria = sessionFactory.openSession().createCriteria(UserDetails.class);
		return criteria.list();
	}

	@Override
	public Boolean addUserDetails(UserDetails userDetails) {
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		session.save(userDetails);
		tx.commit();
		session.close();
		return true;
	}

	public UserDetails getUserByEmail(String email){
        Criteria criteria = sessionFactory.openSession().createCriteria(UserDetails.class);
        UserDetails userDetails =(UserDetails) criteria.add(Restrictions.eq("email", email))
                .uniqueResult();
        return userDetails;
    }

}
