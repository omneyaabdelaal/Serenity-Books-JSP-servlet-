package com.service;



import com.model.Users;

public interface UserServices {
	boolean login(Users user);
	boolean createAccount(Users user);

}
