# Customer KYC Application Documentation

## Project Overview
The Customer KYC Application is a Java-based desktop application designed to collect customer Know Your Customer (KYC) details through a graphical user interface (GUI). The application allows users to input their name, mobile number, and upload Aadhaar and PAN card documents. These documents are then uploaded to an Amazon S3 bucket for secure storage.

## Project Structure
- **CustomerKYCForm.java**  
  This is the main GUI class built using Java Swing. It provides a form for users to enter their personal details and upload KYC documents. The form validates inputs and uses the `S3Uploader` class to upload files to AWS S3.

- **S3Uploader.java**  
  This class handles the interaction with AWS S3 using the AWS SDK for Java v2. It manages the connection to the S3 service and provides a method to upload files to a specified folder within the configured S3 bucket.

- **pom.xml**  
  Maven project configuration file that manages project dependencies and build settings.

## Dependencies
- Java 17 (as specified in the Maven compiler settings)
- AWS SDK for Java v2 (S3 module, version 2.20.19)

## Setup and Build
1. Ensure you have Java 17 installed on your system.
2. Install Maven for project build and dependency management.
3. Clone or download the project source code.
4. Navigate to the project root directory (where `pom.xml` is located).
5. Run the following command to build the project and download dependencies:
   ```
   mvn clean install
   ```

## Usage Instructions
1. After building, run the application by executing the `CustomerKYCForm` class. This can be done from your IDE or via command line:
   ```
   java -cp target/customer-kyc-app-1.0-SNAPSHOT.jar CustomerKYCForm
   ```
2. The GUI window will open with fields for:
   - Name (first and last name required)
   - Mobile Number
   - Upload buttons for Aadhaar Card and PAN Card files
3. Fill in the details and select the respective files using the "Choose File" buttons.
4. Click the "Submit" button to upload the files to the configured AWS S3 bucket.
5. Upon successful upload, a confirmation message will be displayed.

## Configuration Details
- **AWS Credentials:**  
  The AWS access key and secret key are currently hardcoded in the `S3Uploader` class constructor.  
- **S3 Bucket:**  
  The bucket name is set to `"stoic-customers-data"`.  
- **Region:**  
  The AWS region is set to `AP_SOUTH_1` (Asia Pacific - Mumbai).  

To change these settings, modify the `S3Uploader.java` file accordingly.

## Security Note
Hardcoding AWS credentials in source code is not recommended for production environments due to security risks. It is advisable to use environment variables, AWS credentials profiles, or IAM roles to manage credentials securely.

## Possible Enhancements
- Externalize AWS credentials and configuration to a properties file or environment variables.
- Add more robust input validation and error handling.
- Implement logging for upload operations and errors.
- Enhance the GUI with additional features or improved user experience.
- Add unit and integration tests for better maintainability.

---

This documentation provides an overview and instructions to understand, build, and use the Customer KYC Application.

---
## Snapshots
Assets/Screenshot (1).png
