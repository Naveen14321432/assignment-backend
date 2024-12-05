package com.example.demo.dto;

import java.time.LocalDateTime;

public class SubmissionDTO {

    private Long id; 
    private Long assignmentId;
    private String grade;
    public String getGrade() {
		return grade;
	}

	public void setGrade(String grade) {
		this.grade = grade;
	}

	private String studentUsername; 
    private String fileUrl; 
    private LocalDateTime submissionDate; 

    public SubmissionDTO() {}

    public SubmissionDTO(Long id, Long assignmentId, String studentUsername, String fileUrl, LocalDateTime submissionDate) {
        this.id = id;
        this.assignmentId = assignmentId;
        this.studentUsername = studentUsername;
        this.fileUrl = fileUrl;
        this.submissionDate = submissionDate;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAssignmentId() {
        return assignmentId;
    }

    public void setAssignmentId(Long assignmentId) {
        this.assignmentId = assignmentId;
    }

    public String getStudentUsername() {
        return studentUsername;
    }

    public void setStudentUsername(String studentUsername) {
        this.studentUsername = studentUsername;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public LocalDateTime getSubmissionDate() {
        return submissionDate;
    }

    public void setSubmissionDate(LocalDateTime submissionDate) {
        this.submissionDate = submissionDate;
    }
}
