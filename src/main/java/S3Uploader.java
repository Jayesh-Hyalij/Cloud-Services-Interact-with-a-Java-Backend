import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

public class S3Uploader {
    private S3Client s3;
    private String bucketName = "stoic-customers-data"; // Your S3 bucket name

    public S3Uploader() {
        s3 = S3Client.builder()
                .region(Region.AP_SOUTH_1) // Change to your region
                // Use environment variables or other secure methods to provide credentials
                .credentialsProvider(StaticCredentialsProvider.create(
                    AwsBasicCredentials.create(System.getenv("AWS_ACCESS_KEY_ID"), System.getenv("AWS_SECRET_ACCESS_KEY"))
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
        }
    }
}
