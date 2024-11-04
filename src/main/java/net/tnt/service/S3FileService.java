package net.tnt.service;

import io.quarkus.logging.Log;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;
import software.amazon.awssdk.services.s3.model.S3Exception;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.nio.file.Path;

@ApplicationScoped
public class S3FileService {

    private final S3Client s3Client;
    private final Region region = Region.AP_SOUTHEAST_2;

    @Inject
    public S3FileService() {
        try {
            AwsBasicCredentials awsCreds = AwsBasicCredentials.create(
                    "YOUR_ACCESS_KEY",
                    "YOUR_SECRET_KEY");

            this.s3Client = S3Client.builder()
                    .credentialsProvider(StaticCredentialsProvider.create(awsCreds))
                    .region(region)
                    .build();
        } catch (Exception e) {
            Log.error(e);
            throw e;
        }
    }

    public String uploadFile(String bucketName, Path filePath, String keyName) {
        try {
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(keyName)
                    .build();

            PutObjectResponse response = s3Client.putObject(putObjectRequest, filePath);

            return "https:"+"//" + bucketName.trim() + ".s3." + region + ".amazonaws.com/" + keyName.trim();
        } catch (S3Exception e) {
            throw new RuntimeException("Failed to upload file to S3: " + e.awsErrorDetails().errorMessage(), e);
        }
    }
}
