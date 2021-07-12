package com.wani.waniapi.auth.models;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Null;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;
import java.time.LocalDateTime;


@Document(collection = "users")
public class User {
    @Id
    private String id;

    @NotBlank
    @Size(max = 20)
    private String username;

    @NotBlank
    @Size(max = 50)
    @Email
    private String email;


    @NotBlank
    @Size(max = 120)
    private String password;

    @DBRef
    private Set<Role> roles = new HashSet<>();

    private String reference;

    private String firstName;

    private String lastName;

    private String address;

    private String image;

    private Boolean isActive;
    
    private LocalDateTime createdAt;

    private String token;
	private LocalDateTime tokenCreationDate;

    public User() {
    }

    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.isActive = true;
        this.setCreatedAt(LocalDateTime.now());
    }

    public User(String username, String email, String password, Boolean isActive) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.isActive = isActive;
        this.setCreatedAt(LocalDateTime.now());

    }

    public  User(String username, String email, String password, String reference){
        this.username = username;
        this.email = email;
        this.password = password;
        this.reference = reference;
        this.isActive = true;
        this.setCreatedAt(LocalDateTime.now());

    }
    public  User(
        String username, 
        String email, 
        String password, 
        String reference,
        String firstName,
        String lastName,
        String address
    ){
        this.username = username;
        this.email = email;
        this.password = password;
        this.reference = reference;
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.isActive = true;
        this.setCreatedAt(LocalDateTime.now());

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }
    public String getFirstName() { return firstName; }

    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }

    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getAddress() { return address; }

    public void setAddress(String address) { this.address = address; }

    public String getReference() { return reference; }

    public void setReference(String reference) { this.reference = reference; }

    public Boolean getIsActive() { return isActive; }

    public void setIsActive(Boolean isActive) { this.isActive = isActive; }

    public String getImage() { return image; }

    public void setImage(String image) { this.image = image; }

    public String getToken() { return token; }

    public void setToken(String token) { this.token = token; }

    public LocalDateTime getTokenCreationDate() { return tokenCreationDate; }

    public void setTokenCreationDate(LocalDateTime tcd) { this.tokenCreationDate = tcd; }

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}
	
	public UserResponse getRequestResponse() {
		UserResponse userResponse = new UserResponse();
		userResponse.setId(this.id);
		userResponse.setEmail(this.email);
		userResponse.setUsername(this.username);
		return userResponse;
				
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
}
