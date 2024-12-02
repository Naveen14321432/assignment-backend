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

import jakarta.transaction.Transactional;

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
    
    @Transactional
    public List<SubmissionDTO> getAllSubmissions() {
        List<Submission> submissions = submissionRepository.findAll();
        return submissions.stream()
            .map(submission -> {
                SubmissionDTO dto = new SubmissionDTO();
                dto.setId(submission.getId());
                dto.setAssignmentId(submission.getAssignment().getId());
                dto.setStudentUsername(submission.getStudent().getUsername());
                dto.setSubmissionDate(submission.getSubmissionDate());
                dto.setFileUrl(submission.getFileUrl());
                return dto;
            })
            .collect(Collectors.toList());
    }

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

    public SubmissionDTO uploadAssignment(Long assignmentId, String studentUsername, MultipartFile file) throws IOException {
        Assignment assignment = assignmentRepository.findById(assignmentId)
                .orElseThrow(() -> new RuntimeException("Assignment not found"));
        User student = userRepository.findByUsername(studentUsername)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        String uploadDir = "uploads/";
        File directory = new File(uploadDir);
        if (!directory.exists()) {
            directory.mkdir();
        }
        String filePath = uploadDir + file.getOriginalFilename();
        file.transferTo(new File(filePath));

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
        Assignment assignment = assignmentRepository.findById(submissionDTO.getAssignmentId())
                .orElseThrow(() -> new RuntimeException("Assignment not found"));
        User student = userRepository.findByUsername(submissionDTO.getStudentUsername())
                .orElseThrow(() -> new RuntimeException("Student not found"));

        Submission submission = new Submission();
        submission.setAssignment(assignment);
        submission.setStudent(student);
        submission.setFileUrl(submissionDTO.getFileUrl());
        submission.setSubmissionDate(submissionDTO.getSubmissionDate() != null ? submissionDTO.getSubmissionDate() : LocalDateTime.now());

        Submission savedSubmission = submissionRepository.save(submission);

        submissionDTO.setId(savedSubmission.getId());
        submissionDTO.setSubmissionDate(savedSubmission.getSubmissionDate());

        return submissionDTO;  
    }
}
