package com.wani.waniapi.api.models;

import com.wani.waniapi.auth.models.User;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document
public class Subscription {
    @Id
    private String id;
    private User user;
    private Pack pack;
    private Payement payement;
    private Date dateDeDebut;
    private Date dateDeFin;

}
