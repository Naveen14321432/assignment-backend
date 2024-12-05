package com.example.demo.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "assignments")
public class Assignment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String description;
    private LocalDate dueDate;

	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "teacher_id", nullable = false)
    private User teacher;
	
	@OneToMany(mappedBy = "assignment", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Submission> submissions;

    // Default constructor
    public Assignment() {
    }

    // Constructor with ID only
    public Assignment(Long id) {
        this.id = id;
    }

    // Constructor with all fields
    public Assignment(Long id, String title, String description, LocalDate dueDate, User teacher) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.dueDate = dueDate;
        this.teacher = teacher;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public User getTeacher() {
        return teacher;
    }

    public void setTeacher(User teacher) {
        this.teacher = teacher;
    }
}