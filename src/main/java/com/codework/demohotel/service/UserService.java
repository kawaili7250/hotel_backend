package com.codework.demohotel.service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.codework.demohotel.model.User;
import com.codework.demohotel.exception.UserAlreadyExistsException;
import com.codework.demohotel.model.Role;
import com.codework.demohotel.repository.RoleRepository;
import com.codework.demohotel.repository.UserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final RoleRepository roleRepository;
	
	@Override
	public User registerUser(User user) {
		// TODO Auto-generated method stub
		List<Role> roles;
		
		if (userRepository.existsByEmail(user.getEmail())) {
			throw new UserAlreadyExistsException(user.getEmail() + " already exists.");
		}
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		//Role userRole = roleRepository.findByName("ROLE_USER").get();
		
		if (user.getRoles() == null || user.getRoles().isEmpty()) {
			System.out.println("if");
			Role defaultRole = roleRepository.findByName("ROLE_USER")
				.orElseThrow(() -> new RuntimeException("Default role not found"));
			roles = List.of(defaultRole);
		} else {
			System.out.println("else");
			roles = user.getRoles()
				.stream()
				.map(role -> roleRepository.findByName(role.getName()).orElseThrow(() -> new RuntimeException("Role not found"))).toList();
		}
		//user.setRoles(Collections.singletonList(userRole));
		user.setRoles(roles);
		return userRepository.save(user);
	}

	@Override
	public List<User> getUsers() {
		// TODO Auto-generated method stub
		return userRepository.findAll();
	}

	@Transactional
	@Override
	public void deleteUser(String email) {
		// TODO Auto-generated method stub
		User user = getUser(email);
		if (user != null) {
			userRepository.deleteByEmail(email);
		}		
	}

	@Override
	public User getUser(String email) {
		// TODO Auto-generated method stub
		return userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User not found!"));
	}

}
