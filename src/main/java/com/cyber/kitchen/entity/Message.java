package com.cyber.kitchen.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Table
@Getter
@Setter
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @OneToOne(targetEntity = Solution.class, fetch = FetchType.EAGER)
    private Solution solution;

    @OneToOne(targetEntity = User.class, fetch = FetchType.EAGER)
    private User user;

    private String data;

    @OneToMany
    private List<Document> documents;
}
