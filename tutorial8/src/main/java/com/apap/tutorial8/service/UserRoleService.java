package com.apap.tutorial8.service;

import com.apap.tutorial8.model.UserRoleModel;

public interface UserRoleService {
	UserRoleModel addUser(UserRoleModel user);
	UserRoleModel findUserByUsername(String username);
	public String encrypt(String password);
	public void changePassword(UserRoleModel user, String newPassword);
}
