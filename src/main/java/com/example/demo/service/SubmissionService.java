package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.example.demo.dto.SubmissionDTO;
import com.example.demo.model.Assignment;
import com.example.demo.model.Submission;
import com.example.demo.model.User;
import com.example.demo.repository.AssignmentRepository;
import com.example.demo.repository.SubmissionRepository;
import com.example.demo.repository.UserRepository;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SubmissionService {

    @Autowired
    private SubmissionRepository submissionRepository;

    @Autowired
    private AssignmentRepository assignmentRepository;

    @Autowired
    private UserRepository userRepository;

    // Existing JSON-based submission handling
    public SubmissionDTO submitAssignment(SubmissionDTO submissionDTO) {
        Assignment assignment = assignmentRepository.findById(submissionDTO.getAssignmentId())
                .orElseThrow(() -> new RuntimeException("Assignment not found"));
        User student = userRepository.findByUsername(submissionDTO.getStudentUsername())
                .orElseThrow(() -> new RuntimeException("Student not found"));

        Submission submission = new Submission();
        submission.setAssignment(assignment);
        submission.setStudent(student);
        submission.setFileUrl(submissionDTO.getFileUrl());
        submission.setSubmissionDate(LocalDateTime.now());

        Submission savedSubmission = submissionRepository.save(submission);

        submissionDTO.setId(savedSubmission.getId());
        submissionDTO.setSubmissionDate(savedSubmission.getSubmissionDate());
        return submissionDTO;
    }

    // Fetch submissions by student
    public List<SubmissionDTO> getSubmissionsByStudent(Long studentId) {
        return submissionRepository.findByStudentId(studentId).stream()
                .map(submission -> new SubmissionDTO(
                        submission.getId(),
                        submission.getAssignment().getId(),
                        submission.getStudent().getUsername(),
                        submission.getFileUrl(),
                        submission.getSubmissionDate()))
                .collect(Collectors.toList());
    }

    // New method for handling file uploads
    public SubmissionDTO uploadAssignment(Long assignmentId, String studentUsername, MultipartFile file) throws IOException {
        Assignment assignment = assignmentRepository.findById(assignmentId)
                .orElseThrow(() -> new RuntimeException("Assignment not found"));
        User student = userRepository.findByUsername(studentUsername)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        // Save file to uploads directory
        String uploadDir = "uploads/";
        File directory = new File(uploadDir);
        if (!directory.exists()) {
            directory.mkdir();
        }
        String filePath = uploadDir + file.getOriginalFilename();
        file.transferTo(new File(filePath));

        // Create and save submission record
        Submission submission = new Submission();
        submission.setAssignment(assignment);
        submission.setStudent(student);
        submission.setFileUrl(filePath);
        submission.setSubmissionDate(LocalDateTime.now());

        Submission savedSubmission = submissionRepository.save(submission);

        return new SubmissionDTO(
                savedSubmission.getId(),
                savedSubmission.getAssignment().getId(),
                savedSubmission.getStudent().getUsername(),
                savedSubmission.getFileUrl(),
                savedSubmission.getSubmissionDate());
    }
    
    public SubmissionDTO saveSubmission(SubmissionDTO submissionDTO) {
        // Fetch the assignment and student based on the IDs provided in SubmissionDTO
        Assignment assignment = assignmentRepository.findById(submissionDTO.getAssignmentId())
                .orElseThrow(() -> new RuntimeException("Assignment not found"));
        User student = userRepository.findByUsername(submissionDTO.getStudentUsername())
                .orElseThrow(() -> new RuntimeException("Student not found"));

        // Create a Submission entity and populate it with data from the DTO
        Submission submission = new Submission();
        submission.setAssignment(assignment);
        submission.setStudent(student);
        submission.setFileUrl(submissionDTO.getFileUrl());
        submission.setSubmissionDate(submissionDTO.getSubmissionDate() != null ? submissionDTO.getSubmissionDate() : LocalDateTime.now());

        // Save the Submission entity to the database
        Submission savedSubmission = submissionRepository.save(submission);

        // Populate the DTO with the saved entity's details
        submissionDTO.setId(savedSubmission.getId());
        submissionDTO.setSubmissionDate(savedSubmission.getSubmissionDate());

        return submissionDTO;  // Return the populated DTO
    }
}
