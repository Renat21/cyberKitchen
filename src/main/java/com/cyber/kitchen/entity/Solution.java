package com.cyber.kitchen.entity;


import com.cyber.kitchen.enumer.State;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import javax.print.Doc;
import java.util.List;

@Entity
@Table
@Getter
@Setter
public class Solution {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;


    @ManyToOne
    @JoinColumn(name = "task_id")
    private Task task;

    @ManyToOne(cascade = CascadeType.MERGE)
    private Team team;


    private State state;

    private Long curScore;

    @OneToMany
    private List<Document> documents;
}
