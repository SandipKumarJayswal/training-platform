package com.example.catalog.core.course;

import jakarta.persistence.*;

@Entity
@Table(name = "courses")
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String city;

    @Column(nullable = false)
    private int capacity;

    @Column(nullable = false)
    private boolean published;

    protected Course() { }

    public Course(String title, int capacity, String city) {
        this.title = title;
        this.capacity = capacity;
        this.city = city;
        this.published = false;
    }

    public Long getId() { return id; }
    public String getTitle() { return title; }
    public String getCity() { return city; }
    public int getCapacity() { return capacity; }
    public boolean isPublished() { return published; }
    public void publish() { this.published = true; }
}