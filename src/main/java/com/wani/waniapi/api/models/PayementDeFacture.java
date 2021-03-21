package com.wani.waniapi.api.models;

import com.wani.waniapi.auth.models.User;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document
public class PayementDeFacture {
    @Id
    private String id;
    private Service service;
    private User user;
    private MoyenDePayement moyenDePayement;
    private int montant;
    private Date date;
}
