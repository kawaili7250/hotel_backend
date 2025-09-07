package com.codework.demohotel.service;

import java.util.List;
import java.util.Optional;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.codework.demohotel.exception.RoleAlreadyExistException;
import com.codework.demohotel.exception.UserAlreadyExistsException;
import com.codework.demohotel.model.Role;
import com.codework.demohotel.model.User;
import com.codework.demohotel.repository.RoleRepository;
import com.codework.demohotel.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RoleService implements IRoleService {
	private final RoleRepository roleRepository;
	private final IUserService userService;
	private final UserRepository userRepository;

	@Override
	public List<Role> getRoles() {
		// TODO Auto-generated method stub
		return roleRepository.findAll();
	}

	@Override
	public Role createRole(Role theRole) {
		// TODO Auto-generated method stub
		String roleName = "ROLE_" + theRole.getName().toUpperCase();
		Role role = new Role(roleName);
		if (roleRepository.existsByName(roleName)) {
			throw new RoleAlreadyExistException(theRole.getName() + " role already exists!");
		}
		return roleRepository.save(role);
	}

	@Override
	public void deleteRole(Long id) {
		// TODO Auto-generated method stub
		this.removeAllUsersFromRole(id);
		roleRepository.deleteById(id);
	}

	@Override
	public Role findByName(String name) {
		// TODO Auto-generated method stub
		return roleRepository.findByName(name).get();
	}

	@Override
	public User removeUserFromRole(Long userId, Long roleId) {
		// TODO Auto-generated method stub
		Optional<User> user = userRepository.findById(userId);
		Optional<Role> role = roleRepository.findById(roleId);
		if (role.isPresent() && role.get().getUsers().contains(user.get())) {
			role.get().removeUserFromRole(user.get());
			roleRepository.save(role.get());
			return user.get();
		}
		throw new UsernameNotFoundException("User not found");
	}

	@Override
	public User assignRoleToUser(Long userId, Long roleId) {
		// TODO Auto-generated method stub
		Optional<User> user = userRepository.findById(userId);
		Optional<Role> role = roleRepository.findById(roleId);
		if (user.isPresent() && user.get().getRoles().contains(role.get())) {
			throw new UserAlreadyExistsException(user.get().getFirstName() + " is already assigned to the role: " + role.get().getName());
		}
		if (role.isPresent()) {
			role.get().assignRoleToUser(user.get());
			roleRepository.save(role.get());
		}
		return user.get();
	}

	@Override
	public Role removeAllUsersFromRole(Long roleId) {
		// TODO Auto-generated method stub
		Optional<Role> role = roleRepository.findById(roleId);
		role.ifPresent(Role::removeAllUsersFromRole);
		return roleRepository.save(role.get());
	}

}
