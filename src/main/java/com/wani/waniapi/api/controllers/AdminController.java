package com.wani.waniapi.api.controllers;


import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import com.wani.waniapi.auth.models.User;
import com.wani.waniapi.auth.models.Role;
import com.wani.waniapi.auth.models.ERole;
import com.wani.waniapi.api.models.Account;
import com.wani.waniapi.api.models.File;
import com.wani.waniapi.api.models.Payment;
import com.wani.waniapi.api.models.PaymentMethod;
import com.wani.waniapi.api.models.PaymentResponse;
import com.wani.waniapi.api.models.Subscription;
import com.wani.waniapi.auth.repository.UserRepository;
import com.wani.waniapi.auth.services.UserService;
import com.wani.waniapi.auth.repository.RoleRepository;
import com.wani.waniapi.api.repositories.SubscriptionPlanRepository;
import com.wani.waniapi.api.repositories.SubscriptionRepository;
import com.wani.waniapi.api.serializers.SubscriptionSerializer;
import com.wani.waniapi.api.repositories.AccountRepository;
import com.wani.waniapi.api.repositories.FileRepository;
import com.wani.waniapi.api.repositories.PaymentMethodRepository;
import com.wani.waniapi.api.repositories.PaymentRepository;
import com.wani.waniapi.auth.playload.response.ErrorResponse;
import com.wani.waniapi.auth.playload.response.MessageResponse;
import com.wani.waniapi.auth.playload.request.UpdateRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.wani.waniapi.auth.playload.request.SignupRequest;
import com.wani.waniapi.api.playload.request.paymentmethod.CreatePaymentMethodRequest;
import com.wani.waniapi.api.playload.request.subscriptionplan.CreateSubscriptionPlanRequest;
import com.wani.waniapi.api.models.SubscriptionPlan;
import com.wani.waniapi.api.models.SubscriptionPlanResponse;
import com.wani.waniapi.api.models.SubscriptionResponse;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Optional;
import java.util.List;
import java.util.Set;
import org.bson.types.Binary;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/v1/admin")
public class AdminController {

    @Autowired
	private UserService userService;

    @Autowired
    FileRepository fileRepository;
    
    @Autowired
    UserRepository userRepository;
    
    @Autowired
    PaymentMethodRepository paymentMethodRepository;
    
    @Autowired
    PaymentRepository paymentRepository;


    @Autowired
    RoleRepository roleRepository;
    

    @Autowired
    SubscriptionPlanRepository subscriptionPlanRepository;
    
    @Autowired
    SubscriptionRepository subscriptionRepository;
    
    @Autowired
    AccountRepository accountRepository;
    
    
    @Autowired
    PasswordEncoder encoder;
    
    @Autowired
    private JavaMailSender javaMailSender;
    

    /**
     * USERS
     * 
     */
    @GetMapping("/users")
    @PreAuthorize("hasRole('ADMIN')")
    public List<User> getUsers(){
    	
        String img = "http://localhost:8089/api/v1/file/image/";

        List<User> users = userRepository.findAll();
        for(User user: users) {
        	if(user.getImage() != null) {
            	user.setImage(img+user.getImage());
        	}
        }
        return users;
    }

    @GetMapping("/user/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity getUser(@PathVariable String id){
        Optional<User> user =  userRepository.findById(id);
        if(!user.isPresent()){
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        String img = "http://localhost:8089/api/v1/file/image/";
        User userValue = user.get();
        if(userValue.getImage() != null) {
        	userValue.setImage(img+userValue.getImage());
        }
        return ResponseEntity.ok(userValue);
    }
    
    @DeleteMapping("/user/{id}/delete")
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

    @PutMapping("/user/{id}/update")
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
        //check the user signup reference
        if(updateRequest.getPassword() != null){
            //set the user reference
            userValues.setPassword(
            	encoder.encode(updateRequest.getPassword())
            );
        }
        // update the user
        userValues.setUsername(updateRequest.getUsername());
        userValues.setEmail(updateRequest.getEmail());
        userValues.setFirstName(updateRequest.getFirstName());
        userValues.setLastName(updateRequest.getLastName());
        userValues.setAddress(updateRequest.getAddress());
        if(updateRequest.getIsActive() != null) {
            userValues.setIsActive(updateRequest.getIsActive());
        }
        String img = "http://localhost:8089/api/v1/file/image/";
        if(userValues.getImage() != null) {
        	userValues.setImage(img+userValues.getImage());
        }
        //TODO update the user
        return ResponseEntity.ok(userRepository.save(userValues));
    }

    @PutMapping("user/{id}/set-profile-image")
    @PreAuthorize("hasRole('ADMIN')")
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

    @PostMapping("/user/create")
    public ResponseEntity<?> createUser(@Valid @RequestBody SignupRequest signUpRequest) {
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
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

        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
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

        // Create new user's account
        User user = new User(signUpRequest.getUsername(),
                signUpRequest.getEmail(),
                encoder.encode(signUpRequest.getPassword()));
        //check the user signup reference
        if(signUpRequest.getRefUsername() != null){
            //set the user reference
        	String refUsername = signUpRequest.getRefUsername();

            // get the user
            Optional<User> refUser =  userRepository.findByUsername(refUsername);
            // check if the ref user exists
            if(refUser.isPresent()){            		
                  user.setReference(refUser.get().getId());
            }        	
        }
        //check the user signup first name
        if(signUpRequest.getFirstName() != null){
            //set the user first name
            user.setFirstName(signUpRequest.getFirstName());
        }
        //check the user signup last name
        if(signUpRequest.getLastName() != null){
            //set the user last name
            user.setLastName(signUpRequest.getLastName());
        }
        //check the user signup address
        if(signUpRequest.getAddress() != null){
            //set the user address
            user.setAddress(signUpRequest.getAddress());
        }
        //check the user signup active state
        if(signUpRequest.getIsActive() != null){
            //set the user state
            user.setIsActive(signUpRequest.getIsActive());
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
        User createdUser = userRepository.save(user);

        return ResponseEntity.ok(
            createdUser
            );
    }
	
    
    void sendEmail(String email, String subject, String message) {

        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setFrom("hophoet@gmail.com");
        msg.setTo(email);
        
        msg.setSubject(subject);
        msg.setText(message);

        javaMailSender.send(msg);

    }

    @PostMapping("user/forgot-password")
    @PreAuthorize("hasRole('ADMIN')")
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
    

    /*
     * SUBSCRIPTION PLAN
     */
    @PostMapping("/subscription-plan/create")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> createSubscriptionPlan(
           @Valid @RequestBody CreateSubscriptionPlanRequest createSubscriptionPlanRequest
    ) {
    	
    	if(createSubscriptionPlanRequest.getMinAmount() == null) {
    		return ResponseEntity
                    .badRequest()
                    .body(
                        new ErrorResponse(
                                404,
                                "subscription-plan/min-amount-is-required",
                                "subscription plan mininmum amount is required"
                        )
                    );
    	}
    	if(createSubscriptionPlanRequest.getMaxAmount() == null) {
    		return ResponseEntity
                    .badRequest()
                    .body(
                        new ErrorResponse(
                                404,
                                "subscription-plan/max-amount-is-required",
                                "subscription plan maximum amount is required"
                        )
                    );
    	}
    	if(createSubscriptionPlanRequest.getFrequency() == null) {
    		return ResponseEntity
                    .badRequest()
                    .body(
                        new ErrorResponse(
                                404,
                                "subscription-plan/frequency-is-required",
                                "subscription plan frequency is required"
                        )
                    );
    	}
       	if(createSubscriptionPlanRequest.getRoip() == null) {
    		return ResponseEntity
                    .badRequest()
                    .body(
                        new ErrorResponse(
                                404,
                                "subscription-plan/roip-is-required",
                                "subscription plan return on investment percentage is required"
                        )
                    );
    	}
       	if(createSubscriptionPlanRequest.getDuration() == null) {
    		return ResponseEntity
                    .badRequest()
                    .body(
                        new ErrorResponse(
                                404,
                                "subscription-plan/duration-is-required",
                                "subscription plan duration is required"
                        )
                    );
    	}
       	
      	
       	if(createSubscriptionPlanRequest.getMinAmount() > createSubscriptionPlanRequest.getMaxAmount()) {
       		return ResponseEntity
                    .badRequest()
                    .body(
                        new ErrorResponse(
                                404,
                                "subscription-plan/min-amount-biger-than-max-amount",
                                "subscription plan max amount must be big or equal to min amount"
                        )
                    );
       	}

    	

        // Create new subscription plan
        SubscriptionPlan subscriptionPlan = new SubscriptionPlan(
            createSubscriptionPlanRequest.getName(),
            createSubscriptionPlanRequest.getDescription(),
            createSubscriptionPlanRequest.getMinAmount(),
            createSubscriptionPlanRequest.getMaxAmount(),
            createSubscriptionPlanRequest.getFrequency(),
            createSubscriptionPlanRequest.getRoip(),
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
        // check if the subscription plan don't have a subscriptioin in progress
        List<Subscription> subscriptions = subscriptionRepository.findBySubscriptionPlanId(subscriptionPlanValues.getId());
        for(Subscription subscription : subscriptions) {
        		  return ResponseEntity
        	                .badRequest()
        	                .body(
        	                    new ErrorResponse(
        	                            400,
        	                            "subscription-plan/have-subscription-in-progress",
        	                            "subscription plan can not be update, have subscription in progess"
        	                    )
        	      );
        	
        }
    	if(createSubscriptionPlanRequest.getMinAmount() == null) {
    		return ResponseEntity
                    .badRequest()
                    .body(
                        new ErrorResponse(
                                404,
                                "subscription-plan/min-amount-is-required",
                                "subscription plan mininmum amount is required"
                        )
                    );
    	}
    	if(createSubscriptionPlanRequest.getMaxAmount() == null) {
    		return ResponseEntity
                    .badRequest()
                    .body(
                        new ErrorResponse(
                                404,
                                "subscription-plan/max-amount-is-required",
                                "subscription plan maximum amount is required"
                        )
                    );
    	}
    	if(createSubscriptionPlanRequest.getFrequency() == null) {
    		return ResponseEntity
                    .badRequest()
                    .body(
                        new ErrorResponse(
                                404,
                                "subscription-plan/frequency-is-required",
                                "subscription plan frequency is required"
                        )
                    );
    	}
       	if(createSubscriptionPlanRequest.getRoip() == null) {
    		return ResponseEntity
                    .badRequest()
                    .body(
                        new ErrorResponse(
                                404,
                                "subscription-plan/roip-is-required",
                                "subscription plan return on investment percentage is required"
                        )
                    );
    	}
       	if(createSubscriptionPlanRequest.getDuration() == null) {
    		return ResponseEntity
                    .badRequest()
                    .body(
                        new ErrorResponse(
                                404,
                                "subscription-plan/duration-is-required",
                                "subscription plan duration is required"
                        )
                    );
    	}
       	
       	if(createSubscriptionPlanRequest.getMinAmount() > createSubscriptionPlanRequest.getMaxAmount()) {
       		return ResponseEntity
                    .badRequest()
                    .body(
                        new ErrorResponse(
                                404,
                                "subscription-plan/min-amount-biger-than-max-amount",
                                "subscription plan max amount must be big or equal to min amount"
                        )
                    );
       	}
        
        // update the subcription plan
        subscriptionPlanValues.setName(createSubscriptionPlanRequest.getName());
        subscriptionPlanValues.setDescription(createSubscriptionPlanRequest.getDescription());
        subscriptionPlanValues.setMinAmount(createSubscriptionPlanRequest.getMinAmount());
        subscriptionPlanValues.setMaxAmount(createSubscriptionPlanRequest.getMaxAmount());
        subscriptionPlanValues.setRoip(createSubscriptionPlanRequest.getRoip());
        subscriptionPlanValues.setDuration(createSubscriptionPlanRequest.getDuration());
        subscriptionPlanValues.setFrequency(createSubscriptionPlanRequest.getFrequency());
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
            // get the subscription plan object
            SubscriptionPlan subscriptionPlanValues = subscriptionPlanRepository.findById(id).get();
            // check if the subscription plan don't have a subscriptioin in progress
            List<Subscription> subscriptions = subscriptionRepository.findBySubscriptionPlanId(subscriptionPlanValues.getId());
            for(Subscription subscription : subscriptions) {
            		  return ResponseEntity
            	                .badRequest()
            	                .body(
            	                    new ErrorResponse(
            	                            400,
            	                            "subscription-plan/have-subscription-in-progress",
            	                            "subscription plan can not be update, have subscription in progess"
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
    
    public SubscriptionPlanResponse _getSubscriptionPlanResponse(SubscriptionPlan subscriptionPlan) {
    	SubscriptionPlanResponse subscriptionPlanResponse = new SubscriptionPlanResponse();
    	subscriptionPlanResponse.setId(subscriptionPlan.getId());
    	subscriptionPlanResponse.setName(subscriptionPlan.getName());
    	subscriptionPlanResponse.setDescription(subscriptionPlan.getDescription());
    	subscriptionPlanResponse.setMinAmount(subscriptionPlan.getMinAmount());
    	subscriptionPlanResponse.setMaxAmount(subscriptionPlan.getMaxAmount());
    	subscriptionPlanResponse.setFrequency(subscriptionPlan.getFrequency());
    	subscriptionPlanResponse.setRoip(subscriptionPlan.getRoip());
    	subscriptionPlanResponse.setAvailable(subscriptionPlan.getAvailable());
    	subscriptionPlanResponse.setDuration(subscriptionPlan.getDuration());
    	subscriptionPlanResponse.setCreatedAt(subscriptionPlan.getCreatedAt());
    	
    	
    	List<Subscription> spSubscriptions = subscriptionRepository.findBySubscriptionPlanId(subscriptionPlan.getId());
    	
    	subscriptionPlanResponse.setSubscriptionsCount(
    			spSubscriptions.size()
    	);
    	return subscriptionPlanResponse;
    }
    
    @GetMapping("/subscription-plans")
    public List<SubscriptionPlanResponse> getSubscriptionPlans(){
    	List<SubscriptionPlanResponse> subscriptionPlanResponses = new ArrayList<>();
        List<SubscriptionPlan> subscriptionPlans = subscriptionPlanRepository.findAll();
        for(SubscriptionPlan sp: subscriptionPlans) {
        	subscriptionPlanResponses.add(
        			this._getSubscriptionPlanResponse(sp)
        	);
        }
        return subscriptionPlanResponses;
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

    /*
     * SUBSCRIPTIONS
     */
    @GetMapping("/subscription-plan/{subscriptionPlanId}/subscriptions")
    @PreAuthorize("hasRole('ADMIN')")
    public List<Subscription> getSubscriptionPlanSubscriptions(@PathVariable String subscriptionPlanId){
        List<Subscription> subscriptions = subscriptionRepository.findBySubscriptionPlanId(subscriptionPlanId);
        return subscriptions;
    }
    
    @GetMapping("/subscriptions")
    @PreAuthorize("hasRole('ADMIN')")
    public List<SubscriptionResponse> getSubscriptions(){
    	List<SubscriptionResponse> subscriptionResponses = new ArrayList<>();
        List<Subscription> subscriptions = subscriptionRepository.findAll();
        for(Subscription subscription: subscriptions) {
        
        	SubscriptionResponse subscriptionResponse = SubscriptionSerializer.serializer(subscription);
        	subscriptionResponse.setId(subscription.getId());
        	subscriptionResponse.setCreatedAt(subscription.getCreatedAt());
        	subscriptionResponse.setEndedAt(subscription.getEndedAt());
        	subscriptionResponse.setPaid(subscription.getPaid());
        	subscriptionResponse.setAmount(subscription.getAmount());
        	subscriptionResponse.setTimeRemaining(subscription.getTimeRemaining());
        	subscriptionResponse.setEndedAt(subscription.getEndedAt());
        	subscriptionResponse.setLastInterestPaymentAt(subscription.getLastInterestPaymentAt());
        	subscriptionResponse.setNextInterestPaymentAt(subscription.getNextInterestPaymentAt());
        	
        	Optional<Account> account = accountRepository.findById(subscription.getAccountId());

    		if(account.isPresent()) {
    			subscriptionResponse.setAccount(account.get());
    		}
    		Optional<SubscriptionPlan> subscriptionPlan = subscriptionPlanRepository.findById(subscription.getSubscriptionPlanId());
    		if(subscriptionPlan.isPresent()) {
    			subscriptionResponse.setSubscriptionPlan(subscriptionPlan.get());
    		}
    		Optional<Payment> payment = paymentRepository.findById(subscription.getPaymentId());
    		if(payment.isPresent()) {
    			// get the payment model object
    			Payment paymentValues = payment.get();
    			// create the payment response model object
    			PaymentResponse paymentResponse = new PaymentResponse();
    			// set the payment model object id and createdAt attribute to the payment response
    			paymentResponse.setId(paymentValues.getId());
    			// get the payment method object with the payment method id from the payment object
    			paymentResponse.setCreatedAt(paymentValues.getCreatedAt());
    			Optional<PaymentMethod> paymentMethod = paymentMethodRepository.findById(paymentValues.getPaymentMethodId());
        		if(paymentMethod.isPresent()) {
        			// set the payment method object to payment response if it exist
        			paymentResponse.setPaymentMethod(paymentMethod.get());
        		}
        		subscriptionResponse.setPayment(paymentResponse);

    		}
    		subscriptionResponses.add(subscriptionResponse);
        	
        }
        return subscriptionResponses;
    }
    
    @GetMapping("/subscription-plan/{subscriptionPlanId}/accounts")
    @PreAuthorize("hasRole('ADMIN')")
    public List<Account> getSubscriptionPlanAccounts(@PathVariable String subscriptionPlanId){
        List<Subscription> subscriptions = subscriptionRepository.findBySubscriptionPlanId(subscriptionPlanId);
        List<Account> accounts = new ArrayList<>();
        for(Subscription subscription : subscriptions) {
        	String accountId = subscription.getAccountId();
        
            // get account
            Optional<Account> account =  accountRepository.findById(accountId);

            // check if the user account exists
            if(account.isPresent()){
            	accounts.add(account.get());
            }
        }
        return accounts;
    }

  
    /*
     * PAYMENT METHOD
     */
    @GetMapping("/payment-methods")
    @PreAuthorize("hasRole('ADMIN')")
    public List<PaymentMethod> getPaymentMethods(){
        List<PaymentMethod> paymentMethods = paymentMethodRepository.findAll();
        return paymentMethods;
    }
    
    @PutMapping("/payment-method/{id}/toggle")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity togglePaymentMethodState(
        @PathVariable String id
    ){
    	   // get the user
        Optional<PaymentMethod> paymentMethod =  paymentMethodRepository.findById(id);
        // check if the user exists
        if(!paymentMethod.isPresent()){
              return ResponseEntity
                    .badRequest()
                    .body(
                        new ErrorResponse(
                                404,
                                "payment-method/not-found",
                                "Payment method not found, invalid payment method id"
                        )

                    );

        }
        PaymentMethod paymentMethodValue = paymentMethod.get();
        paymentMethodValue.setActive(!paymentMethodValue.isActive());
        
        PaymentMethod updatePaymentMethod = paymentMethodRepository.save(paymentMethodValue);
 
        return ResponseEntity.ok(updatePaymentMethod);
        
    	
    }
    
    @PostMapping("/payment-method/create")
    public ResponseEntity<?> createPaymentMethod(@Valid @RequestBody CreatePaymentMethodRequest createPaymentMethodRequest) {
        PaymentMethod paymentMethod = new PaymentMethod(
        		createPaymentMethodRequest.getName(), 
        		createPaymentMethodRequest.getDescription()
        );
        if(createPaymentMethodRequest.getIsActive() != null) {
        	paymentMethod.setActive(createPaymentMethodRequest.getIsActive());
        }
        PaymentMethod createdPaymentMethod = paymentMethodRepository.save(paymentMethod);
        return ResponseEntity.ok(createdPaymentMethod);
    }
    
    @PutMapping("/payment-method/{id}/update")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity updatePaymentMethod(
        @PathVariable String id, 
        @Valid @RequestBody CreatePaymentMethodRequest createPaymentMethodRequest
    ){
        // get the user
        Optional<PaymentMethod> paymentMethod =  paymentMethodRepository.findById(id);

        if(!paymentMethod.isPresent()) {
            return ResponseEntity
                    .badRequest()
                    .body(
                        new ErrorResponse(
                                404,
                                "payment-method/not-found",
                                "Payment method not found, invalid payment method id"
                        )
                    );
        }
        PaymentMethod paymentMethodValues = paymentMethod.get();
        paymentMethodValues.setName(createPaymentMethodRequest.getName());
        paymentMethodValues.setDescription(createPaymentMethodRequest.getDescription());
        if(createPaymentMethodRequest.getIsActive() != null) {
        	paymentMethodValues.setActive(createPaymentMethodRequest.getIsActive());
        }
        PaymentMethod updatedPaymentMethod = paymentMethodRepository.save(paymentMethodValues);
        return ResponseEntity.ok(updatedPaymentMethod);

    }
    
    


}
