package com.wani.waniapi.auth.controllers;

import com.wani.waniapi.auth.models.ERole;
import com.wani.waniapi.auth.models.Role;
import com.wani.waniapi.auth.models.User;
import com.wani.waniapi.auth.playload.request.LoginRequest;
import com.wani.waniapi.auth.playload.request.SignupRequest;
import com.wani.waniapi.auth.playload.request.UpdateRequest;
import com.wani.waniapi.auth.playload.response.JwtResponse;
import com.wani.waniapi.auth.playload.response.ErrorResponse;

import com.wani.waniapi.api.models.File;

import com.wani.waniapi.api.repositories.FileRepository;

import com.wani.waniapi.auth.playload.response.MessageResponse;
import com.wani.waniapi.auth.repository.RoleRepository;
import com.wani.waniapi.auth.repository.UserRepository;
import com.wani.waniapi.auth.security.jwt.JwtUtils;
import com.wani.waniapi.auth.security.services.UserDetailsImpl;


import com.wani.waniapi.auth.services.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.HttpStatus;

import javax.validation.Valid;
import java.util.Date;
import java.util.HashSet;
import java.util.Optional;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.bson.types.Binary;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api")
public class AuthController {

    @Autowired
	private UserService userService;
    
    @Autowired
    FileRepository fileRepository;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder encoder;
    
    @Autowired
    private JavaMailSender javaMailSender;
    


    @Autowired
    JwtUtils jwtUtils;

    @PostMapping("/auth/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());

        if (!userDetails.getIsActive()) {
            return ResponseEntity
                    .badRequest()
                    .body(
                        new ErrorResponse(
                                400,
                                "auth/account-is-not-active",
                                "Account was been deactivate"
                        )
                    );
        }

        String img = null;
        String imageId = userDetails.getImage();
        if(imageId!=null){
            Optional<File> image =  fileRepository.findById(imageId);
            if(image.isPresent()){
                File imageValues = image.get();
                img = "http://localhost:8089/api/v1/file/image/"+ imageValues.getId();
            }
        }
        return ResponseEntity.ok(
            new JwtResponse(
                jwt,
                userDetails.getId(),
                userDetails.getUsername(),
                userDetails.getEmail(),
                roles,
                userDetails.getReference(),
                userDetails.getFirstName(),
                userDetails.getLastName(),
                userDetails.getAddress(),
                userDetails.getIsActive(),
                img
            )
        );
    }

    @PostMapping("/auth/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
              return ResponseEntity
                    .badRequest()
                    .body(
                        new ErrorResponse(
                                400,
                                "auth/username-already-used",
                                "Username is already taken!"
                        )

                    );
        }

        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(
                        new ErrorResponse(
                                400,
                                "auth/email-already-used",
                                "Email is already taken!"
                        )
                    );
        }



        // Create new user's account
        User user = new User(signUpRequest.getUsername(),
                signUpRequest.getEmail(),
                encoder.encode(signUpRequest.getPassword()));
        //check the user signup reference
        if(signUpRequest.getReference() != null){
            //set the user reference
            user.setReference(signUpRequest.getReference());
        }

        //check the user signup state
        if(signUpRequest.getIsActive() != null){
            //set the user reference
            user.setIsActive(true);
        }

        Set<String> strRoles = signUpRequest.getRoles();
        Set<Role> roles = new HashSet<>();

        if (strRoles == null) {
            Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                switch (role) {
                    case "admin":
                        Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(adminRole);

                        break;
                    case "mod":
                        Role modRole = roleRepository.findByName(ERole.ROLE_MODERATOR)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(modRole);

                        break;
                    default:
                        Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(userRole);
                }
            });
        }

        user.setRoles(roles);
        userRepository.save(user);


        //signin the user

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(signUpRequest.getUsername(), signUpRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> user_roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());

        return ResponseEntity.ok(
            new JwtResponse(
                jwt,
                userDetails.getId(),
                userDetails.getUsername(),
                userDetails.getEmail(),
                user_roles,
                userDetails.getReference(),
                userDetails.getFirstName(),
                userDetails.getLastName(),
                userDetails.getAddress(),
                userDetails.getIsActive(),
                userDetails.getImage()

            )
        );


    }


    @PutMapping("/auth/{id}/update")
    public ResponseEntity<?> updateUser(
        @PathVariable String id, 
        @Valid @RequestBody UpdateRequest updateRequest
    ) {

        // get the user
        Optional<User> user =  userRepository.findById(id);
        // check if the user exists
        if(!user.isPresent()){
              return ResponseEntity
                    .badRequest()
                    .body(
                        new ErrorResponse(
                                404,
                                "user/not-found",
                                "User not found, invalid user id"
                        )

                    );

        }

        // get the user object
        User userValues = user.get();
        // check if the username is not already used
        if (
            userRepository.existsByUsername(updateRequest.getUsername()) && 
            !userValues.getUsername().equals(updateRequest.getUsername())

        ){
              return ResponseEntity
                    .badRequest()
                    .body(
                        new ErrorResponse(
                                400,
                                "auth/username-already-used",
                                "Username is already taken!"
                        )

                    );
        }

        // check if the email is not already used
        if (
            userRepository.existsByEmail(updateRequest.getEmail()) &&
            !userValues.getEmail().equals(updateRequest.getEmail())
         ) {
            return ResponseEntity
                    .badRequest()
                    .body(
                        new ErrorResponse(
                                400,
                                "auth/email-already-used",
                                "Email is already taken!"
                        )
                    );
        }
        // update the user
        userValues.setUsername(updateRequest.getUsername());
        userValues.setEmail(updateRequest.getEmail());
        userValues.setFirstName(updateRequest.getFirstName());
        userValues.setLastName(updateRequest.getLastName());
        userValues.setAddress(updateRequest.getAddress());
        //TODO update the user
        userRepository.save(userValues);

        return ResponseEntity.ok(
           userValues
            );


    }


    @PutMapping("auth/{id}/set-profile-image")
    public ResponseEntity setUserProfileImage(
            @PathVariable String id, 
            @RequestParam("image") MultipartFile image
        ) {
        
        // get the user
        Optional<User> user =  userRepository.findById(id);
        // check if the user exists
        if(!user.isPresent()){
              return ResponseEntity
                    .badRequest()
                    .body(
                        new ErrorResponse(
                                404,
                                "user/not-found",
                                "User not found, invalid user id"
                        )

                    );

        }
        // get the user object
        User userValues = user.get();
        

        String imageId = userValues.getImage();
        if(imageId!=null){
            Optional<File> imageFile =  fileRepository.findById(imageId);
            if(imageFile.isPresent()){
                // delete the old image setted
                fileRepository.deleteById(imageId);
            }
        }


        String message = "";
        try {
            File uploadFile = new File();
            uploadFile.setName(image.getOriginalFilename());
            uploadFile.setCreatedAt(new Date());
            uploadFile.setContent(new Binary(image.getBytes()));
            uploadFile.setContentType(image.getContentType());
            uploadFile.setSize(image.getSize());

            File savedFile = fileRepository.save(uploadFile);
            userValues.setImage(savedFile.getId());
            userRepository.save(userValues);

            String url = "http://localhost:8089/api/v1/file/image/"+ savedFile.getId();
            userValues.setImage(url);

            return ResponseEntity.ok(
                userValues
            );
        } catch (Exception e) {
        message = "Could not upload the file: " + image.getOriginalFilename() + "!";
        return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(
            new ErrorResponse(
                    404,
                    "code",
                    message
            )
            );
        }
    }

    void sendEmail(String email, String subject, String message) {

        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setFrom("hophoet@gmail.com");
        msg.setTo(email);
        
        msg.setSubject(subject);
        msg.setText(message);

        javaMailSender.send(msg);

    }


	@PostMapping("auth/forgot-password")
	public ResponseEntity forgotPassword(@RequestParam String email) {

		String response = userService.forgotPassword(email);

		if (!response.startsWith("Invalid")) {
			String url = "http://localhost:8089/api/auth/reset-password?token=" + response;
			try {
				this.sendEmail(email, "Reset your password", url);
	            return new ResponseEntity<>(
	                url,
	                HttpStatus.OK
	             );
				
			} catch (Exception e) {
				// TODO: handle exception
		        return ResponseEntity
				 .badRequest()
		            .body(
		                new ErrorResponse(
		                        404,
		                        "user/password-reset-failed",
		                        "Password reset mail sent failed"
		                )

		            );
			}
		}
        return ResponseEntity
            .badRequest()
            .body(
                new ErrorResponse(
                        404,
                        "user/not-found",
                        "User not found with this email"
                )

            );

	}

	@PutMapping("auth/reset-password")
	public ResponseEntity  resetPassword(@RequestParam String token,
			@RequestParam String password) {

		String result =  userService.resetPassword(token, password);
        if(result == "token-not-valid"){
            return ResponseEntity
                .badRequest()
                .body(
                    new ErrorResponse(
                            400,
                            "token/not-valid",
                            "invalid token"
                    )
                );
        }
        else if(result == "token-was-expired"){
            return ResponseEntity
                .badRequest()
                .body(
                    new ErrorResponse(
                            400,
                            "token/was-expired",
                            "token was expired"
                    )
                );
        }

        return new ResponseEntity<>(
            "password changed successfully!",
            HttpStatus.OK
        );

	}


}
