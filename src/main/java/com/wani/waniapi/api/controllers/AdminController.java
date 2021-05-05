package com.wani.waniapi.api.controllers;

import com.wani.waniapi.auth.models.User;
import com.wani.waniapi.auth.models.Role;
import com.wani.waniapi.auth.models.ERole;
import com.wani.waniapi.auth.repository.UserRepository;
import com.wani.waniapi.auth.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.wani.waniapi.auth.playload.response.MessageResponse;
import com.wani.waniapi.auth.playload.request.SignupRequest;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Optional;
import java.util.List;
import java.util.Set;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api")
public class AdminController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;
    
    @Autowired
    PasswordEncoder encoder;

    @GetMapping("/users")
    @PreAuthorize("hasRole('ADMIN')")
    public List<User> getUsers(){
        List<User> users = userRepository.findAll();
        return users;
    }

    @GetMapping("/users/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity getUser(@PathVariable String id){
        Optional<User> user =  userRepository.findById(id);
        if(!user.isPresent()){
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(user);
    }
    

    @DeleteMapping("/users/{id}/delete")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity deleteUser(@PathVariable String id){
         try {
            if(!userRepository.findById(id).isPresent()){
                return new ResponseEntity(HttpStatus.NOT_FOUND);
            }
            userRepository.deleteById(id);
            return new ResponseEntity(HttpStatus.OK);
        }
        catch (Exception e){
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }


    @PutMapping("/users/{id}/update")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity updateUser(
        @PathVariable String id, 
        @RequestPart(required = true) String username,
        @RequestPart(required = true) String email
    ){
        // get the user
        Optional<User> user =  userRepository.findById(id);
        // check if the user exists
        if(!user.isPresent()){
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: user not exists!"));
        }
        // get the user object
        User userValues = user.get();
        // check if the username is not already used
        if (
            userRepository.existsByUsername(username) && 
            !userValues.getUsername().equals(username)

        ){
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Username is already taken!"));
        }

        // check if the email is not already used
        if (
            userRepository.existsByEmail(email) &&
            !userValues.getEmail().equals(email)
         ) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Email is already in use!"));
        }

        // update the user
        userValues.setUsername(username);
        userValues.setEmail(email);
        //TODO update the user
        return ResponseEntity.ok(userRepository.save(userValues));
    }


    @PostMapping("/users/create")
    public ResponseEntity<?> createUser(@Valid @RequestBody SignupRequest signUpRequest) {
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Username is already taken!"));
        }

        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Email is already in use!"));
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

        return ResponseEntity.ok(new MessageResponse("User created successfully!"));
    }
}
