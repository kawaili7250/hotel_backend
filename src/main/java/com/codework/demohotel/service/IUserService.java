package com.codework.demohotel.service;

import java.util.List;
import java.util.Optional;

import com.codework.demohotel.model.User;

public interface IUserService {
	User registerUser(User user);
	
	List<User> getUsers();
	
	void deleteUser(String email);
	
	User getUser(String email);
}
