package com.article.dao;

import java.util.List;

import com.article.model.UserDetails;

public interface UserDao {
	
	List<UserDetails> getUserDetails();
	Boolean addUserDetails(UserDetails userDetails);
	UserDetails getUserByEmail(String email);
}
