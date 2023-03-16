package com.cyber.kitchen.entity;


import com.cyber.kitchen.enumer.EventRole;
import com.cyber.kitchen.enumer.Role;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table
@Getter
@Setter
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @OneToOne
    private User user;

    @JsonFormat(pattern="dd/MM/yyyy HH:mm:ss")
    private LocalDateTime entranceDate;

    private EventRole role;

    private String teamName;
}
