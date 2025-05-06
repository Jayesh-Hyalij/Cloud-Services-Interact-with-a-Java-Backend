import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;

public class CustomerKYCForm extends JFrame {
    private JTextField nameField;
    private JTextField mobileField;
    private JLabel aadhaarFileLabel;
    private JLabel panFileLabel;
    private File aadhaarFile;
    private File panFile;

    private S3Uploader s3Uploader;

    public CustomerKYCForm() {
        setTitle("Customer KYC Form");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        s3Uploader = new S3Uploader();

        // Create panel and layout
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        // Name label and field
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Name:"), gbc);

        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        nameField = new JTextField(20);
        panel.add(nameField, gbc);

        // Mobile label and field
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.NONE;
        panel.add(new JLabel("Mobile No:"), gbc);

        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        mobileField = new JTextField(20);
        panel.add(mobileField, gbc);

        // Aadhaar upload button and label
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.NONE;
        panel.add(new JLabel("Upload Aadhaar Card:"), gbc);

        gbc.gridx = 1;
        JButton aadhaarButton = new JButton("Choose File");
        panel.add(aadhaarButton, gbc);

        gbc.gridx = 2;
        aadhaarFileLabel = new JLabel("No file chosen");
        panel.add(aadhaarFileLabel, gbc);

        aadhaarButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            int option = fileChooser.showOpenDialog(this);
            if (option == JFileChooser.APPROVE_OPTION) {
                aadhaarFile = fileChooser.getSelectedFile();
                aadhaarFileLabel.setText(aadhaarFile.getName());
            }
        });

        // PAN upload button and label
        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(new JLabel("Upload PAN Card:"), gbc);

        gbc.gridx = 1;
        JButton panButton = new JButton("Choose File");
        panel.add(panButton, gbc);

        gbc.gridx = 2;
        panFileLabel = new JLabel("No file chosen");
        panel.add(panFileLabel, gbc);

        panButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            int option = fileChooser.showOpenDialog(this);
            if (option == JFileChooser.APPROVE_OPTION) {
                panFile = fileChooser.getSelectedFile();
                panFileLabel.setText(panFile.getName());
            }
        });

        // Submit button
        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        JButton submitButton = new JButton("Submit");
        panel.add(submitButton, gbc);

        submitButton.addActionListener(e -> {
            String name = nameField.getText().trim();
            String mobile = mobileField.getText().trim();

            if (name.isEmpty() || mobile.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter both name and mobile number.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (aadhaarFile == null) {
                JOptionPane.showMessageDialog(this, "Please upload Aadhaar card.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (panFile == null) {
                JOptionPane.showMessageDialog(this, "Please upload PAN card.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Parse first and last name
            String[] nameParts = name.split("\\s+");
            if (nameParts.length < 2) {
                JOptionPane.showMessageDialog(this, "Please enter both first and last name.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            String folderName = nameParts[0] + "_" + nameParts[1];

            try {
                s3Uploader.uploadFile(folderName, aadhaarFile);
                s3Uploader.uploadFile(folderName, panFile);

                String message = "Name: " + name + "\n" +
                                 "Mobile No: " + mobile + "\n" +
                                 "Aadhaar Card: " + aadhaarFile.getName() + "\n" +
                                 "PAN Card: " + panFile.getName() + "\n\n" +
                                 "Files uploaded successfully to S3 folder: " + folderName;

                JOptionPane.showMessageDialog(this, message, "Success", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Failed to upload files: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        add(panel);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            CustomerKYCForm form = new CustomerKYCForm();
            form.setVisible(true);
        });
    }
}
