import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;

import java.util.logging.Logger;

public class S3Uploader {
    private static final Logger logger = Logger.getLogger(S3Uploader.class.getName());
    private S3Client s3;
    private String bucketName = "stoic-customers-data"; // Your S3 bucket name

    public S3Uploader() {
        String accessKey = "AKIASM6YNCLVM6UTDHWX";
        String secretKey = "CxRLCrCEFte6N3A6TYEDVa5pPFgmfTqeNqzJavca";

        s3 = S3Client.builder()
                .region(Region.AP_SOUTH_1) // Change to your region
                .credentialsProvider(StaticCredentialsProvider.create(
                    AwsBasicCredentials.create(accessKey, secretKey)
                ))
                .build();
    }

    public void uploadFile(String folderName, File file) throws IOException {
        String key = folderName + "/" + file.getName();

        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .build();

        try (FileInputStream fis = new FileInputStream(file)) {
            s3.putObject(putObjectRequest, RequestBody.fromInputStream(fis, file.length()));
            logger.info("File uploaded successfully: " + key);
        } catch (S3Exception e) {
            logger.severe("S3 upload failed: " + e.awsErrorDetails().errorMessage());
            throw new IOException("Failed to upload file to S3: " + e.awsErrorDetails().errorMessage(), e);
        }
    }
}
