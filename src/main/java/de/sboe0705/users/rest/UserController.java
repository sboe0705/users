package de.sboe0705.users.rest;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.NOT_FOUND;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import de.sboe0705.users.data.UserRepository;
import de.sboe0705.users.model.User;

@RestController
public class UserController {

	@Autowired
	private UserRepository userRepository;

	@PostMapping("/users/new")
	public void createUser(@RequestBody User user) {
		if (StringUtils.isBlank(user.getId())) {
			throw new ResponseStatusException(BAD_REQUEST, "Mandatory User ID is not provided!");
		}
		if (userRepository.existsById(user.getId())) {
			throw new ResponseStatusException(CONFLICT, "User ID already exists!");
		}
		userRepository.save(user);
	}

	@GetMapping("/users")
	public List<User> getUsers() {
		List<User> users = new ArrayList<>();
		userRepository.findAll() //
				.forEach(users::add);
		return users;
	}

	@GetMapping("/user/{id}")
	public User getUser(@PathVariable String id) {
		return userRepository.findById(id) //
				.orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "User ID does not exist!"));
	}

}
