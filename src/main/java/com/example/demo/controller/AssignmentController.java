package com.example.demo.controller;
import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.dto.AssignmentDTO;
import com.example.demo.service.AssignmentService;
import com.example.demo.service.FileService;

@RestController
@RequestMapping("/api/assignments")
public class AssignmentController {

    @Autowired
    private AssignmentService assignmentService;
    
    @Autowired
    private FileService fileService;


    @PostMapping(consumes = "multipart/form-data")
    public AssignmentDTO createAssignment(
            @RequestParam("title") String title,
            @RequestParam("dueDate") String dueDate,
            @RequestParam("teacherUsername") String teacherUsername,
            @RequestParam("file") MultipartFile file) throws IOException {

        // Save the file and get its path
        String filePath = fileService.saveFile(file);

        // Create the assignment DTO
        AssignmentDTO assignmentDTO = new AssignmentDTO();
        assignmentDTO.setTitle(title);
        assignmentDTO.setDescription(filePath); // Save the file path as the description
        assignmentDTO.setDueDate(java.time.LocalDate.parse(dueDate)); // Ensure the date is parsed correctly
        assignmentDTO.setTeacherUsername(teacherUsername);

        return assignmentService.createAssignment(assignmentDTO);
    }
    
    @GetMapping
    public List<AssignmentDTO> getAllAssignments() {
        return assignmentService.getAllAssignments();
    }

}