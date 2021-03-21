package com.wani.waniapi.api.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Pack {
    @Id
    private String id;
    private String description;
    private int montant;
}
