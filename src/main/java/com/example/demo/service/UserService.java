package com.example.demo.service;

import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public String registerUser(User user) {
        if (userRepository.existsByUsername(user.getUsername())) {
            return "Username already exists";
        }
        
        if (userRepository.existsByEmail(user.getEmail())) {
            return "Email already exists";
        }
        
        if (user.getPassword() == null || user.getPassword().isEmpty()) {
            return "Password cannot be null or empty";
        }

        // Remove password encoding
        // user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return "Registered Successfully";
    }
    
    public void registerAdmin(User user) {
        user.setRole("admin"); // Set the role to admin
        // Remove password encoding
        // user.setPassword(passwordEncoder.encode(user.getPassword())); 
        userRepository.save(user);
    }
    
    public String loginUser(User user) {
        Optional<User> existingUser = userRepository.findByUsername(user.getUsername());

        if (existingUser.isPresent()) {
            User foundUser = existingUser.get();
            if (user.getPassword().equals(foundUser.getPassword())) {
                if ("admin".equalsIgnoreCase(foundUser.getRole())) {
                    return "Admin login successful"; // Admin login successful
                }else if("teacher".equalsIgnoreCase(foundUser.getRole())) {
                	return "Teacher login successful";
                } else if("student".equalsIgnoreCase(foundUser.getRole())){
                    return "Student login successful"; // Regular user login successful
                } else {
                	return "User role doesnot recognized";
                }
            } else {
                return "Invalid password"; // Password incorrect
            }
        } else {
            return "User not found"; // User does not exist
        }
    }

    public Optional<User> getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public String updateUser(String username, User updatedUser) {
        Optional<User> existingUser = userRepository.findByUsername(username);

        if (existingUser.isPresent()) {
            User userToUpdate = existingUser.get();
            userToUpdate.setName(updatedUser.getName());
            userToUpdate.setEmail(updatedUser.getEmail());
            userToUpdate.setDepartment(updatedUser.getDepartment());
            userToUpdate.setRole(updatedUser.getRole());

            if (updatedUser.getPassword() != null && !updatedUser.getPassword().isEmpty()) {
                // Remove password encoding
                // userToUpdate.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
                userToUpdate.setPassword(updatedUser.getPassword()); // Save as plain text
            }

            userRepository.save(userToUpdate);
            return "Updated record successfully";
        } else {
            return "User not found";
        }
    }

    @Transactional
    public String deleteUser(String username) {
        if (userRepository.existsByUsername(username)) {
            userRepository.deleteByUsername(username);
            return "Deleted Successfully";
        } else {
            return "User not found";
        }
    }
}
