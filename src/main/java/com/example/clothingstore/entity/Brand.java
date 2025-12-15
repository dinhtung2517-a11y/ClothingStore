package com.example.clothingstore.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "Brands")
@Data
public class Brand {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    private String country;

    @Column(name = "is_active")
    private Boolean isActive = true;
}
