package com.wani.waniapi.api.controllers;

import com.wani.waniapi.auth.models.User;
import com.wani.waniapi.auth.models.Role;
import com.wani.waniapi.auth.models.ERole;

import com.wani.waniapi.auth.repository.UserRepository;
import com.wani.waniapi.auth.repository.RoleRepository;
import com.wani.waniapi.api.repositories.SubscriptionPlanRepository;

import com.wani.waniapi.auth.playload.response.ErrorResponse;
import com.wani.waniapi.auth.playload.response.MessageResponse;
import com.wani.waniapi.auth.playload.request.UpdateRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import com.wani.waniapi.auth.playload.request.SignupRequest;
import com.wani.waniapi.api.playload.request.subscriptionplan.CreateSubscriptionPlanRequest;
import com.wani.waniapi.api.models.SubscriptionPlan;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Optional;
import java.util.List;
import java.util.Set;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/v1")
public class AdminController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    SubscriptionPlanRepository subscriptionPlanRepository;
    
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
        @Valid @RequestBody UpdateRequest updateRequest
    ){
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
                                "user/username-already-used",
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
                                "user/email-already-used",
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
        userValues.setIsActive(updateRequest.getIsActive());
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



    // Subscription plans management by the administrator
    @PostMapping("/subscription-plan/create")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> createSubscriptionPlan(
           @Valid @RequestBody CreateSubscriptionPlanRequest createSubscriptionPlanRequest
    ) {

        // Create new subscription plan
        SubscriptionPlan subscriptionPlan = new SubscriptionPlan(
            createSubscriptionPlanRequest.getName(),
            createSubscriptionPlanRequest.getDescription(),
            createSubscriptionPlanRequest.getAmount(),
            createSubscriptionPlanRequest.getInterest(),
            createSubscriptionPlanRequest.getDuration(),
            createSubscriptionPlanRequest.getAvailable()
        );
        return ResponseEntity.ok(
            subscriptionPlanRepository.save(subscriptionPlan)
        );
    }


    @PutMapping("/subscription-plan/{id}/update")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity updateSubscriptionPlan(
        @PathVariable String id, 
        @Valid @RequestBody CreateSubscriptionPlanRequest createSubscriptionPlanRequest
    ){
        // get the subscription plan
        Optional<SubscriptionPlan> subscriptionPlan =  subscriptionPlanRepository.findById(id);
        // check if the subscription plan exists
        if(!subscriptionPlan.isPresent()){
            return ResponseEntity
                .badRequest()
                .body(
                    new ErrorResponse(
                            404,
                            "subscription-plan/not-found",
                            "invalid subscription plan id"
                    )
                );
        }
        // get the subscription plan object
        SubscriptionPlan subscriptionPlanValues = subscriptionPlan.get();
        // update the subcription plan
        subscriptionPlanValues.setName(createSubscriptionPlanRequest.getName());
        subscriptionPlanValues.setDescription(createSubscriptionPlanRequest.getDescription());
        subscriptionPlanValues.setAmount(createSubscriptionPlanRequest.getAmount());
        subscriptionPlanValues.setInterest(createSubscriptionPlanRequest.getInterest());
        subscriptionPlanValues.setDuration(createSubscriptionPlanRequest.getDuration()); // update the subscription plan
        subscriptionPlanValues.setAvailable(createSubscriptionPlanRequest.getAvailable());
        return ResponseEntity.ok(subscriptionPlanRepository.save(subscriptionPlanValues));
    }

    

    @DeleteMapping("/subscription-plan/{id}/delete")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity deleteSubcriptionPlan(@PathVariable String id){
         try {
            if(!subscriptionPlanRepository.findById(id).isPresent()){
                return ResponseEntity
                    .badRequest()
                    .body(
                        new ErrorResponse(
                                404,
                                "subscription-plan/not-found",
                                "invalid subscription plan id"
                        )
                    );
            }
            subscriptionPlanRepository.deleteById(id);
            return new ResponseEntity(HttpStatus.OK);
        }
        catch (Exception e){
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }


    @GetMapping("/subscription-plans")
    public List<SubscriptionPlan> getSubscriptionPlans(){
        List<SubscriptionPlan> subscriptionPlans = subscriptionPlanRepository.findAll();
        return subscriptionPlans;
    }



    @GetMapping("/subscription-plan/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity getSubscriptionPlan(@PathVariable String id){
        Optional<SubscriptionPlan> subscriptionPlan =  subscriptionPlanRepository.findById(id);
        if(!subscriptionPlan.isPresent()){
            return ResponseEntity
                .badRequest()
                .body(
                    new ErrorResponse(
                        404,
                        "subscription-plan/not-found",
                        "invalid subscription plan id"
                    )
                );
        }
        return ResponseEntity.ok(subscriptionPlan);
    }





}
