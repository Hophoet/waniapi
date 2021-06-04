package com.wani.waniapi.api.controllers;

import com.wani.waniapi.auth.models.User;

import com.wani.waniapi.auth.models.Role;
import com.wani.waniapi.auth.models.ERole;
import com.wani.waniapi.api.models.File;

import com.wani.waniapi.auth.repository.UserRepository;
import com.wani.waniapi.auth.repository.RoleRepository;
import com.wani.waniapi.api.repositories.SubscriptionPlanRepository;
import com.wani.waniapi.api.repositories.FileRepository;

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
import org.springframework.http.MediaType;

import com.wani.waniapi.auth.playload.request.SignupRequest;
import com.wani.waniapi.api.playload.request.subscriptionplan.CreateSubscriptionPlanRequest;
import com.wani.waniapi.api.models.SubscriptionPlan;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import java.util.Date;
import javax.validation.Valid;
import java.util.Optional;
import org.bson.types.Binary;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/v1")
public class FileController {

    @Autowired
    FileRepository fileRepository;


    @PostMapping("/upload")
    public ResponseEntity uploadFile(@RequestParam("file") MultipartFile file) {
        String message = "";
        try {
            File uploadFile = new File();
            uploadFile.setName(file.getOriginalFilename());
            uploadFile.setCreatedAt(new Date());
            uploadFile.setContent(new Binary(file.getBytes()));
            uploadFile.setContentType(file.getContentType());
            uploadFile.setSize(file.getSize());

            File savedFile = fileRepository.save(uploadFile);
            String url = "http://localhost:8089/api/v1/file/image/"+ savedFile.getId();
            System.out.println(url);

        message = "Uploaded the file successfully: " + file.getOriginalFilename();
        return ResponseEntity.status(HttpStatus.OK).body(
            new ErrorResponse(
                    404,
                    "code",
                    message
            )
            )
            ;
        } catch (Exception e) {
        message = "Could not upload the file: " + file.getOriginalFilename() + "!";
        return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(
            new ErrorResponse(
                    404,
                    "code",
                    message
            )
            );
        }
    }


    @GetMapping(value = "/file/image/{id}", produces = {MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE})
    public byte[] image(@PathVariable String id){
        byte[] data = null;
        Optional<File> file  = fileRepository.findById(id);
        if(file.isPresent()){
            data = file.get().getContent().getData();
        }
        return data;
    }

}
