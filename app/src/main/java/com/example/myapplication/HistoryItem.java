package com.example.myapplication;

import com.google.firebase.firestore.DocumentId;

public class HistoryItem {

    @DocumentId
    private String documentId;

    private String image_url;
    private String detection_result;

    // Required default constructor
    public HistoryItem() {
        // Empty constructor needed for Firestore
    }

    // Constructor with parameters
    public HistoryItem(String image_url, String detection_result) {
        this.image_url = image_url;
        this.detection_result = detection_result;
    }

    // Getter and setter for documentId
    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    // Getter and setter for image_url
    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    // Getter and setter for detection_result
    public String getDetection_result() {
        return detection_result;
    }

    public void setDetection_result(String detection_result) {
        this.detection_result = detection_result;
    }

    // Additional getters for convenience
    public String getImageUrl() {
        return getImage_url();
    }

    public String getResult() {
        return getDetection_result();
    }
}
