package com.cyber.kitchen.entity;


import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table
@Getter
@Setter
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    private String description;

    @JsonFormat(pattern="dd/MM/yyyy HH:mm")
    private LocalDateTime startDate;

    private Long maxScore;

    private String name;

    private Long numeration;

    private Boolean open = Boolean.FALSE;
}
