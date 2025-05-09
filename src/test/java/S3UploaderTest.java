import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class S3UploaderTest {

    @BeforeEach
    public void setUp() {
        clearEnv("AWS_ACCESS_KEY_ID");
        clearEnv("AWS_SECRET_ACCESS_KEY");
    }

    @AfterEach
    public void tearDown() {
        // No-op
    }

    @Test
    public void testConstructorThrowsExceptionWhenCredentialsMissing() {
        Exception exception = assertThrows(IllegalStateException.class, () -> {
            new S3Uploader();
        });
        assertEquals("AWS credentials are not set in environment variables.", exception.getMessage());
    }

    @Test
    public void testUploadFileThrowsIOExceptionForInvalidFile() {
        setEnv("AWS_ACCESS_KEY_ID", "dummyAccessKey");
        setEnv("AWS_SECRET_ACCESS_KEY", "dummySecretKey");

        S3Uploader s3Uploader = new S3Uploader();

        File invalidFile = new File("nonexistentfile.txt");
        IOException exception = assertThrows(IOException.class, () -> {
            s3Uploader.uploadFile("testFolder", invalidFile);
        });
        assertTrue(exception.getMessage().contains("Failed to upload file to S3"));
    }

    private void setEnv(String key, String value) {
        try {
            java.lang.reflect.Field field = System.getenv().getClass().getDeclaredField("m");
            field.setAccessible(true);
            @SuppressWarnings("unchecked")
            java.util.Map<String, String> env = (java.util.Map<String, String>) field.get(System.getenv());
            env.put(key, value);
        } catch (Exception e) {
            throw new RuntimeException("Failed to set environment variable", e);
        }
    }

    private void clearEnv(String key) {
        try {
            java.lang.reflect.Field field = System.getenv().getClass().getDeclaredField("m");
            field.setAccessible(true);
            @SuppressWarnings("unchecked")
            java.util.Map<String, String> env = (java.util.Map<String, String>) field.get(System.getenv());
            env.remove(key);
        } catch (Exception e) {
            throw new RuntimeException("Failed to clear environment variable", e);
        }
    }
}
