package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.example.demo.dto.SubmissionDTO;
import com.example.demo.service.FileService;
import com.example.demo.service.SubmissionService;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/submissions")
public class SubmissionController {

    @Autowired
    private SubmissionService submissionService;

    @Autowired
    private FileService fileService;

    @PostMapping
    public SubmissionDTO submitAssignment(@RequestBody SubmissionDTO submissionDTO) {
        return submissionService.submitAssignment(submissionDTO);
    }

    @GetMapping("/submissions/student/{studentId}")
    public ResponseEntity<?> getSubmissionsByStudent(@PathVariable Long studentId) {
        if (studentId == null) {
            return ResponseEntity.badRequest().body("Invalid student ID");
        }

        // Continue with fetching the submissions
        List<SubmissionDTO> submissions = submissionService.getSubmissionsByStudent(studentId);
        return ResponseEntity.ok(submissions);
    }

    
    @GetMapping("/assignment/{assignmentId}")
    public ResponseEntity<List<SubmissionDTO>> getSubmissionsByAssignment(@PathVariable Long assignmentId) {
        List<SubmissionDTO> submissions = submissionService.getSubmissionsByAssignment(assignmentId);
        return ResponseEntity.ok(submissions);
    }


    @PostMapping("/upload")
    public ResponseEntity<SubmissionDTO> uploadAssignment(
            @RequestParam("assignmentId") Long assignmentId,
            @RequestParam("studentUsername") String studentUsername,
            @RequestParam("file") MultipartFile file) {
        try {
            String fileUrl = fileService.saveFile(file); 

            SubmissionDTO submissionDTO = new SubmissionDTO();
            submissionDTO.setAssignmentId(assignmentId);
            submissionDTO.setStudentUsername(studentUsername);
            submissionDTO.setFileUrl(fileUrl);
            submissionDTO.setSubmissionDate(LocalDateTime.now());

            submissionDTO = submissionService.saveSubmission(submissionDTO);

            return ResponseEntity.ok(submissionDTO);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(null);
        }
    }
    
    @GetMapping
    public List<SubmissionDTO> getAllSubmissions() {
        return submissionService.getAllSubmissions();
    }
    
    @PutMapping("/{id}/grade")
    public ResponseEntity<SubmissionDTO> gradeSubmission(@PathVariable Long id, @RequestBody SubmissionDTO gradeRequest) {
        try {
            String grade = gradeRequest.getGrade();
            SubmissionDTO submissionDTO = submissionService.gradeSubmission(id, grade);
            return ResponseEntity.ok(submissionDTO); 
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

}
