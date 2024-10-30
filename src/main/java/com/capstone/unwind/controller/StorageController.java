package com.capstone.unwind.controller;

import com.capstone.unwind.service.ServiceImplement.S3Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/s3/file")
public class StorageController {
    @Autowired
    private S3Service s3Service;

    @PostMapping("/upload")
    public ResponseEntity<List<String>> uploadFile(@RequestParam(value="file") List<MultipartFile> file){
        return new ResponseEntity<>(s3Service.uploadFiles(file), HttpStatus.OK);
    }
    @DeleteMapping("/delete/{fileName}")
    public ResponseEntity<String> deleteFile(@PathVariable String fileName){
        return new ResponseEntity<>(s3Service.deleteFile(fileName),HttpStatus.OK);
    }
}
