package com.wani.waniapi.auth.services;

import java.time.*;
import java.util.*;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.wani.waniapi.auth.models.User;
import com.wani.waniapi.auth.models.UserResponse;
import com.wani.waniapi.auth.repository.UserRepository;

@Service
public class UserService {

	private static final long EXPIRE_TOKEN_AFTER_MINUTES = 10000;

	@Autowired
	private UserRepository userRepository;


    @Autowired
    PasswordEncoder encoder;

	public String forgotPassword(String email) {

		Optional<User> userOptional = userRepository.findByEmail(email); 
		if (!userOptional.isPresent()) {
			return "Invalid email id.";
		}

		User user = userOptional.get();
		user.setToken(generateToken());
		user.setTokenCreationDate(LocalDateTime.now());

		user = userRepository.save(user);

		return user.getToken();
	}

	public String resetPassword(String token, String password) {

		Optional<User> userOptional = userRepository.findByToken(token); 
		if (!userOptional.isPresent()) {
			return "token-not-valid";
		}

		LocalDateTime tokenCreationDate = userOptional.get().getTokenCreationDate();

		if (isTokenExpired(tokenCreationDate)) {
			return "token-was-expired";

		}
		User user = userOptional.get();
		user.setPassword(
            encoder.encode(password)
        );
		user.setToken(null);
		user.setTokenCreationDate(null);

		userRepository.save(user);

		return "done";
	}
	


	/**
	 * Generate unique token. You may add multiple parameters to create a strong
	 * token.
	 * 
	 * @return unique token
	 */
	private String generateToken() {
		StringBuilder token = new StringBuilder();

		return token.append(UUID.randomUUID().toString())
				.append(UUID.randomUUID().toString()).toString();
	}

	/**
	 * Check whether the created token expired or not.
	 * 
	 * @param tokenCreationDate
	 * @return true or false
	 */
	private boolean isTokenExpired(final LocalDateTime tokenCreationDate) {

		LocalDateTime now = LocalDateTime.now();
		Duration diff = Duration.between(tokenCreationDate, now);
	

		return diff.toMinutes() >= EXPIRE_TOKEN_AFTER_MINUTES;
	}
	
	
	public UserResponse  getRequestResponse(String userId) {
		UserResponse userResponse = new UserResponse();
		Optional<User> user = userRepository.findById(userId);
		if(!user.isPresent()) {
			return null;
		}
		User userObject = user.get();
		userResponse.setId(userObject.getId());
		userResponse.setEmail(userObject.getEmail());
		userResponse.setUsername(userObject.getUsername());
		return userResponse;
	}
}