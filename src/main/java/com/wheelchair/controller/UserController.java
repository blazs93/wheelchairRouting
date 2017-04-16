package com.wheelchair.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wheelchair.db.model.User;
import com.wheelchair.db.model.UserRole;
import com.wheelchair.db.repository.UserRepository;
import com.wheelchair.db.repository.UserRoleRepository;

@Controller // This means that this class is a Controller
public class UserController {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private UserRoleRepository userRoleRepository;

	@RequestMapping("/addUser")
	public String addNewUser(@RequestParam String username, @RequestParam String name,
			@RequestParam String password) {
		// @ResponseBody means the returned String is the response, not a view
		// name
		// @RequestParam means it is a parameter from the GET or POST request

		User user = new User();
		user.setActive(true);
		user.setUsername(username);
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		String hashedPassword = passwordEncoder.encode(password);
		user.setPassword(hashedPassword);
		user.setName(name);

		UserRole userRole = new UserRole();
		userRole.setRole("ROLE_USER");
		userRole.setUser(user);

		userRoleRepository.save(userRole);

		return "redirect:/index.html";  
	}

	@RequestMapping("/all")
	public @ResponseBody Iterable<User> getAllUsers() {
		// This returns a JSON or XML with the users
		return userRepository.findAll();
	}
	
	@RequestMapping("/user")
	public @ResponseBody Iterable<User> getUsers() {
		// This returns a JSON or XML with the users
		return userRepository.findUsers();
	}
	
	@RequestMapping("/updateUser")
	public @ResponseBody void updateUser(@RequestParam Boolean active, @RequestParam String username) {
		userRepository.updateUserActive(active, username);
	}
}
