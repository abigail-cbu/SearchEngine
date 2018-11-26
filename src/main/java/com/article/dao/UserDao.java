package com.article.dao;

import com.article.model.UserDetails;

import java.util.List;

public interface UserDao {
	
	List<UserDetails> getUserDetails();
	Boolean addUserDetails(UserDetails userDetails);
	UserDetails getUserByEmail(String email);
}
