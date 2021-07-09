package com.wani.waniapi.api.services;

import java.io.IOException;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileStorageService {

  public void store(MultipartFile file) throws IOException {
    System.out.println("inside store");
    String fileName = StringUtils.cleanPath(file.getOriginalFilename());
    System.out.println("File name");
    System.out.println(fileName);

    // FileDB FileDB = new FileDB(fileName, file.getContentType(), file.getBytes());

    // return fileDBRepository.save(FileDB);
  }

//   public FileDB getFile(String id) {
//     // return fileDBRepository.findById(id).get();
//   }
  
//   public Stream<FileDB> getAllFiles() {
//     return fileDBRepository.findAll().stream();
//   }
}
