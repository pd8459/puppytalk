package com.example.puppytalk;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Service
public class FileUploadService {

    @Value("${file.upload-dir}")
    private String uploadDir;

    public String upload(MultipartFile multipartFile) throws IOException {
        if(multipartFile.isEmpty()) {
            return null;
        }

        File uploadPath = new File(uploadDir);
        if(!uploadPath.exists()) {
            uploadPath.mkdirs();
        }
        String originalFilename = multipartFile.getOriginalFilename();
        String storedFileName = UUID.randomUUID() + "." + extractExt(originalFilename);

        multipartFile.transferTo(new File(uploadPath, storedFileName));

        return "/images/" + storedFileName;
    }

    private String extractExt(String originalFilename) {
        int pos = originalFilename.lastIndexOf(".");
        return originalFilename.substring(pos + 1);
    }

    public void delete(String imageUrl) {
        if(imageUrl == null || imageUrl.isEmpty()) {
            return;
        }

        String fileName = imageUrl.substring(imageUrl.lastIndexOf("/") + 1);
        File file = new File(uploadDir + fileName);

        if(file.exists()) {
            file.delete();
        }
    }

}
