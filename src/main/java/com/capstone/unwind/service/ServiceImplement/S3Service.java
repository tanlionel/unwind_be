package com.capstone.unwind.service.ServiceImplement;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class S3Service{

    @Value("${aws.s3.bucket.name}")
    private String bucketName;

    @Autowired
    private AmazonS3 s3Client;

    @Value("${aws.bucket.url}")
    private String url;

    public List<String> uploadFiles(List<MultipartFile> files) {
        List<String> uploadedFileNames = new ArrayList<>();

        for (MultipartFile file : files) {
            try {
                File fileObj = convertMultiPartToFile(file);
                String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
                s3Client.putObject(new PutObjectRequest(bucketName, fileName, fileObj));
                fileObj.delete();
                uploadedFileNames.add(url + "/" +fileName);
            } catch (Exception e) {
                log.error("Error uploading file: " + file.getOriginalFilename(), e);
                uploadedFileNames.add("Failed to upload: " + file.getOriginalFilename());
            }
        }

        return uploadedFileNames;
    }
    public String deleteFile(String fileName){
        s3Client.deleteObject(bucketName,fileName);
        return fileName+" removed ...";
    }

    private File convertMultiPartToFile(MultipartFile file) {
        File convFile = new File(file.getOriginalFilename());
        try (FileOutputStream fos = new FileOutputStream(convFile)) {
            fos.write(file.getBytes());
        } catch (IOException e) {
            log.error("Error converting multipartFile to file", e);
        }
        return convFile;
    }

}
