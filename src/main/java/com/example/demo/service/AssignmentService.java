package com.example.demo.service;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.dto.AssignmentDTO;
import com.example.demo.model.Assignment;
import com.example.demo.model.User;
import com.example.demo.repository.AssignmentRepository;
import com.example.demo.repository.UserRepository;

import jakarta.transaction.Transactional;


@Service
public class AssignmentService {

    @Autowired
    private AssignmentRepository assignmentRepository;

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public AssignmentDTO createAssignment(AssignmentDTO assignmentDTO) {
        Assignment assignment = new Assignment(); // Instance created inside the method
        User teacher = userRepository.findByUsername(assignmentDTO.getTeacherUsername())
                .orElseThrow(() -> new RuntimeException("Teacher not found"));

        assignment.setTeacher(teacher);
        assignment.setTitle(assignmentDTO.getTitle());
        assignment.setDescription(assignmentDTO.getDescription());
        assignment.setDueDate(assignmentDTO.getDueDate());

        Assignment savedAssignment = assignmentRepository.save(assignment);
        assignmentDTO.setId(savedAssignment.getId());
        return assignmentDTO;
    }
    
    @Transactional
    public List<AssignmentDTO> getAllAssignments() {
        List<Assignment> assignments = assignmentRepository.findAll();

        return assignments.stream().map(assignment -> {
            AssignmentDTO dto = new AssignmentDTO();
            dto.setId(assignment.getId());
            dto.setTitle(assignment.getTitle());
            dto.setDescription(assignment.getDescription());
            dto.setDueDate(assignment.getDueDate());
            dto.setTeacherUsername(assignment.getTeacher().getUsername());
            return dto;
        }).toList();
    }

}