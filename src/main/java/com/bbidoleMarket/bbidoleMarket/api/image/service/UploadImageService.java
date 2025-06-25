package com.bbidoleMarket.bbidoleMarket.api.image.service;

import com.bbidoleMarket.bbidoleMarket.api.image.enums.ImageFolder;
import com.bbidoleMarket.bbidoleMarket.common.exception.BadRequestException;
import com.bbidoleMarket.bbidoleMarket.common.reponse.ErrorStatus;
import com.google.cloud.WriteChannel;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.UUID;

@Service
public class UploadImageService {

    private final String bucketName;
    private final Storage storage;


    @Autowired
    public UploadImageService(@Value("${spring.cloud.gcp.storage.bucket}") String bucketName, Storage storage) {
        this.bucketName = bucketName;
        this.storage = storage;
    }

    /**
     * 이미지를 지정된 폴더에 업로드하고 URL을 반환합니다
     */

    public String uploadImage(MultipartFile image, ImageFolder folder) throws IOException {
        if (image.isEmpty()) {
            throw new BadRequestException(ErrorStatus.IMAGE_MISSING_EXCEPTION.getMessage());
        }

        String folderPath = folder.getPath();

        // 고유한 파일명 생성 (UUID + 원본 파일명)
        String originalFilename = image.getOriginalFilename();
        String filename = UUID.randomUUID().toString() + "_" + originalFilename;

        // 전체 경로 생성 (폴더 + 파일명)
        String objectName = folderPath + filename;

        // 파일 타입 결정
        String contentType = image.getContentType();
        if (contentType == null) {
            contentType = "application/octet-stream";
        }

        // GCS에 업로드
        BlobId blobId = BlobId.of(bucketName, objectName);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId)
                .setContentType(contentType)
                .build();

        try (WriteChannel writer = storage.writer(blobInfo)) {
            byte[] buffer = new byte[1024];
            try (InputStream input = image.getInputStream()) {
                int limit;
                while ((limit = input.read(buffer)) >= 0) {
                    writer.write(ByteBuffer.wrap(buffer, 0, limit));
                }
            }
        }

        // 이미지 URL 반환
        return String.format("https://storage.googleapis.com/%s/%s", bucketName, objectName);
    }
}