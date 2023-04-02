package com.cyber.kitchen.entity;


import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table
@Getter
@Setter
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    private String name;

    @ManyToOne
    private User organizer;

    private Boolean running;

    @JsonFormat(pattern="dd/MM/yyyy HH:mm")
    private LocalDateTime startDate;

    @JsonFormat(pattern="dd/MM/yyyy HH:mm")
    private LocalDateTime endDate;

    private String memberToken;

    private String expertToken;

    private Long maxMembersInTeam;

    @ManyToMany(cascade = CascadeType.MERGE)
    private List<User> experts = new ArrayList<>();

    @OneToMany(cascade = CascadeType.MERGE)
    private List<Member> members = new ArrayList<>();

    @OneToMany
    private List<Team> teams = new ArrayList<>();

    @OneToMany
    private List<Task> taskList = new ArrayList<>();

    @OneToMany
    private List<Theme> themes = new ArrayList<>();

}
