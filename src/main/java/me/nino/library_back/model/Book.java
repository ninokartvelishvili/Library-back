package me.nino.library_back.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Data
@Entity
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String author;

    @Column(nullable = false)
    private String genre;

    @ManyToMany(mappedBy = "borrowedBooks")
    private List<User> borrowers;
}
