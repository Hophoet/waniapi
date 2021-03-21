package com.wani.waniapi.api.models;

import com.wani.waniapi.auth.models.User;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Compte {
    @Id
    private String id;
    private User user;
}
