/**
 * 
 */
package com.article.service;

import java.util.List;

import com.article.model.UserDetails;

public interface UserService {

	List<UserDetails> getUserDetails();
	Boolean addUserDetails(UserDetails userDetails);
	UserDetails getUserByEmail(String email);
}
