package com.wani.waniapi.api.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Commision {
    @Id
    private String id;
    private Subscription subscription;
    private int montant;
}
