package com.codework.demohotel.service;

import java.util.List;

import com.codework.demohotel.model.Role;
import com.codework.demohotel.model.User;

public interface IRoleService {
	List<Role> getRoles();
	
	Role createRole(Role role);
	
	void deleteRole(Long id);
	
	Role findByName(String name);
	
	User removeUserFromRole(Long userId, Long roleId);
	
	User assignRoleToUser(Long userId, Long roleId);
	
	Role removeAllUsersFromRole(Long roleId);
}
