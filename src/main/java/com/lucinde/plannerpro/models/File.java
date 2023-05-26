package com.lucinde.plannerpro.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "files")
public class File {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String filename;

    // Eerst gekozen voor Byte[] maar dan gaat hibernate klagen als je een INSERT wilt doen,
    // nog uitzoeken of dit toch een byte[] of BLOB moet worden
    private String pathToImage;
    private String description;
}
