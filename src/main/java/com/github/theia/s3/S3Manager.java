package com.github.theia.s3;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@RequiredArgsConstructor
@Component
public class S3Manager {

    @Value("${spring.cloud.aws.s3.bucket}")
    private String bucketName;
    private final AmazonS3 amazonS3;

    public String uploadImages(MultipartFile image) throws IOException {

        String fileName = UUID.randomUUID() + image.getOriginalFilename();
        PutObjectRequest request = new PutObjectRequest(
                bucketName, fileName, image.getInputStream(), getObjectMetadata(image)
        ).withCannedAcl(CannedAccessControlList.PublicRead);
        amazonS3.putObject(request);

        return getFileUrl(fileName);
    }

    private ObjectMetadata getObjectMetadata(MultipartFile image) {
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(image.getSize());
        objectMetadata.setContentType(image.getContentType());
        return objectMetadata;
    }

    public String getFileUrl(String fileName) {
        return amazonS3.getUrl(bucketName, fileName).toString();
    }
}
