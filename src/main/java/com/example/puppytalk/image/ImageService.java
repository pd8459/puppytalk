package com.example.puppytalk.image;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class ImageService {

    @Value("${file.upload-dir}")
    private String uploadPath;

    public String storeFile(MultipartFile multipartFile) throws IOException {
        if (multipartFile == null || multipartFile.isEmpty()) {
            return null;
        }

        String originalFilename = multipartFile.getOriginalFilename();
        String storeFileName = createStoreFileName(originalFilename);

        File directory = new File(uploadPath);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        File saveFile = Paths.get(uploadPath, storeFileName).toFile();
        multipartFile.transferTo(saveFile);

        return "/images/" + storeFileName;
    }

    private String createStoreFileName(String originalFilename) {
        String ext = extractExt(originalFilename);
        String uuid = UUID.randomUUID().toString();
        return uuid + "." + ext;
    }

    private String extractExt(String originalFilename) {
        if (originalFilename == null) return "png"; // 기본값 설정
        int pos = originalFilename.lastIndexOf(".");
        if (pos == -1) return "png";
        return originalFilename.substring(pos + 1);
    }
}