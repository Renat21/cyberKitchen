package com.cyber.kitchen.entity;


import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table
@Data
@Getter
@Setter
public class Team {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    private String name;

    @ManyToOne(cascade = CascadeType.MERGE)
    private Theme theme;

    @OneToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name="team_members",
            joinColumns = {@JoinColumn(name="team_id")},
            inverseJoinColumns = {@JoinColumn(name="member_id")}
    )
    private List<Member> members = new ArrayList<>();
}
