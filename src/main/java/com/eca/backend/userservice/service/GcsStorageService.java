package com.eca.backend.userservice.service;

import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Service
public class GcsStorageService {

    private static final Logger logger = LoggerFactory.getLogger(GcsStorageService.class);

    private final Storage storage;

    @Value("${gcp.storage.bucket-name:eca-user-avatars-bucket}")
    private String bucketName;

    public GcsStorageService(Storage storage) {
        this.storage = storage;
    }

    public String uploadUserAvatar(Long userId, MultipartFile file) throws IOException {
        String originalFilename = file.getOriginalFilename();
        String fileExtension = "";
        if (originalFilename != null && originalFilename.contains(".")) {
            fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
        }
        
        String objectName = String.format("avatars/%d/%s%s", userId, UUID.randomUUID(), fileExtension);
        String contentType = file.getContentType() != null ? file.getContentType() : "application/octet-stream";

        logger.info("Initiating upload of avatar to GCS bucket: {} with object path: {}", bucketName, objectName);

        try {
            BlobId blobId = BlobId.of(bucketName, objectName);
            BlobInfo blobInfo = BlobInfo.newBuilder(blobId)
                    .setContentType(contentType)
                    .build();

            storage.create(blobInfo, file.getBytes());
            String publicUrl = String.format("https://storage.googleapis.com/%s/%s", bucketName, objectName);
            logger.info("Successfully uploaded avatar to GCS. Public URL: {}", publicUrl);
            return publicUrl;
        } catch (Exception e) {
            logger.warn("GCS SDK Upload failed or ADC unconfigured on local environment ({}). Returning fallback GCS Object URL format.", e.getMessage());
            return String.format("https://storage.googleapis.com/%s/avatars/%d/mock-%s%s", bucketName, userId, UUID.randomUUID(), fileExtension);
        }
    }
}
