package com.wani.waniapi.api.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Service {
    @Id
    private String id;
    private String nom;
    private String description;
}
