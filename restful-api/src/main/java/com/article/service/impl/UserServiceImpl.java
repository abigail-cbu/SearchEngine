/**
 * 
 */
package com.article.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.article.dao.UserDao;
import com.article.model.UserDetails;
import com.article.service.UserService;

@Service
public class UserServiceImpl implements UserService {
	
	@Autowired
	private UserDao userDao;

	public List<UserDetails> getUserDetails() {
		return userDao.getUserDetails();

	}

	public Boolean addUserDetails(UserDetails userDetails){
		return userDao.addUserDetails(userDetails);
	}

    @Override
    public UserDetails getUserByEmail(String email) {
        return userDao.getUserByEmail(email);
    }

}
