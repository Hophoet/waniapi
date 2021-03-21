package com.wani.waniapi.api.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

public class MoyenDePayement {
    @Id
    private String id;
    private String name;
    private String description;
}
