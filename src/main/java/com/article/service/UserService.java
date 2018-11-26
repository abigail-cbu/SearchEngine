/**
 * 
 */
package com.article.service;

import com.article.model.UserDetails;

import java.util.List;

public interface UserService {

	List<UserDetails> getUserDetails();
	Boolean addUserDetails(UserDetails userDetails);
	UserDetails getUserByEmail(String email);
}
