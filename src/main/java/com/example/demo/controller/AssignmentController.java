package com.example.demo.controller;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.example.demo.dto.AssignmentDTO;
import com.example.demo.service.AssignmentService;

@RestController
@RequestMapping("/api/assignments")
public class AssignmentController {

    @Autowired
    private AssignmentService assignmentService;

    @PostMapping
    public AssignmentDTO createAssignment(@RequestBody AssignmentDTO assignmentDTO) {
        return assignmentService.createAssignment(assignmentDTO);
    }
    
    @GetMapping
    public List<AssignmentDTO> getAllAssignments() {
        return assignmentService.getAllAssignments();
    }

}