# S3Uploader Class Code Explanation

This document explains the logic and functionality of the `S3Uploader` Java class, which is responsible for uploading files to an Amazon S3 bucket.

---

## Imports

```java
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
```

- These imports include Java IO classes for file handling and AWS SDK classes for S3 operations.
- `Logger` is used for logging information and errors.

---

## Class Declaration and Members

```java
public class S3Uploader {
    private static final Logger logger = Logger.getLogger(S3Uploader.class.getName());
    private S3Client s3;
    private String bucketName = "stoic-customers-data"; // Your S3 bucket name
```

- `logger`: Used to log messages for success or failure of uploads.
- `s3`: An instance of the AWS S3 client used to interact with the S3 service.
- `bucketName`: The name of the S3 bucket where files will be uploaded.

---

## Constructor

```java
public S3Uploader() {
    String accessKey = "AccessKey"; // Replace with your access key
    String secretKey = "secretKey"; // Replace with your secret key

    s3 = S3Client.builder()
            .region(Region.AP_SOUTH_1) // Change to your region
            .credentialsProvider(StaticCredentialsProvider.create(
                AwsBasicCredentials.create(accessKey, secretKey)
            ))
            .build();
}
```

- Initializes the `S3Client` with:
  - AWS region (`AP_SOUTH_1` in this example).
  - Static credentials provider using the provided access key and secret key.
- **Note:** Replace `"AccessKey"` and `"secretKey"` with your actual AWS credentials or use a more secure method like environment variables or IAM roles.

---

## `uploadFile` Method

```java
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
```

### Explanation:

- **Parameters:**
  - `folderName`: The folder (prefix) in the S3 bucket where the file will be uploaded.
  - `file`: The `File` object representing the file to upload.

- **Key Construction:**
  - The S3 object key is constructed by concatenating the `folderName` and the file's name with a `/` separator.
  - This simulates a folder structure in S3.

- **PutObjectRequest:**
  - Builds the request specifying the target bucket and the object key.

- **File Upload:**
  - Opens a `FileInputStream` to read the file.
  - Calls `s3.putObject` to upload the file content to S3.
  - Logs success message on completion.

- **Error Handling:**
  - Catches `S3Exception` if the upload fails.
  - Logs the error message.
  - Throws an `IOException` to propagate the failure.

---

## Summary

- The `S3Uploader` class provides a simple interface to upload files to a specified S3 bucket.
- It uses AWS SDK v2 for Java to handle authentication and file upload.
- Folder structure in S3 is simulated by using the folder name as a prefix in the object key.
- Proper logging and error handling are implemented to track upload status.

---

## Recommendations

- Avoid hardcoding AWS credentials in code. Use environment variables, AWS profiles, or IAM roles for better security.
- Consider adding methods for other S3 operations as needed.
- Implement retries or exponential backoff for robustness in production environments.
