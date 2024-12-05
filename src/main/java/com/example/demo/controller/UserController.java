package com.example.demo.controller;

import com.example.demo.dto.UserDTO;
import com.example.demo.model.User;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    private UserDTO convertToDTO(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setDepartment(user.getDepartment());
        userDTO.setName(user.getName());
        userDTO.setEmail(user.getEmail());
        userDTO.setRole(user.getRole());
        userDTO.setUsername(user.getUsername());
        return userDTO;
    }

    private User convertToEntity(UserDTO userDTO) {
        User user = new User();
        user.setDepartment(userDTO.getDepartment());
        user.setName(userDTO.getName());
        user.setEmail(userDTO.getEmail());
        user.setRole(userDTO.getRole());
        user.setUsername(userDTO.getUsername());
        user.setPassword(userDTO.getPassword());
        return user;
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody UserDTO userDTO) {
        User user = convertToEntity(userDTO);
        return ResponseEntity.ok(userService.registerUser(user));
    }
    
    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> loginUser(@RequestBody UserDTO userDTO) {
        User user = convertToEntity(userDTO);
        String loginResponse = userService.loginUser(user);
        
        Map<String, Object> response = new HashMap<>();
        response.put("message", loginResponse);

        if (loginResponse.equals("Admin login successful")) {
            response.put("role", "admin");
        } else if (loginResponse.equals("Teacher login successful")) {
            response.put("role", "teacher");
        } else if (loginResponse.equals("Student login successful")) {
            response.put("role", "student");
        } else {
        	response.put("role", "unknown");
        }

        return ResponseEntity.ok(response);
    }

    @GetMapping("/allusers")
    public List<UserDTO> getAllUsers(){
        return userService.getAllUsers().stream()
                .map(this::convertToDTO)
                .toList();
    }

    @GetMapping("/{username}")
    public ResponseEntity<UserDTO> getUserByUsername(@PathVariable String username) {
        Optional<User> user = userService.getUserByUsername(username);
        return user.map(value -> ResponseEntity.ok(convertToDTO(value)))
                   .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{username}")
    public ResponseEntity<String> updateUser(@PathVariable String username, @RequestBody UserDTO updatedUserDTO) {
        User updatedUser = convertToEntity(updatedUserDTO);
        return ResponseEntity.ok(userService.updateUser(username, updatedUser));
    }

    @DeleteMapping("/{username}")
    public ResponseEntity<String> deleteUser(@PathVariable String username) {
        return ResponseEntity.ok(userService.deleteUser(username));
    }
}
