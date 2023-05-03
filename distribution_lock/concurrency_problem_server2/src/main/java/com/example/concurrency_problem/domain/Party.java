package com.example.concurrency_problem.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Party {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private int capacity;

    @OneToMany(mappedBy = "party")
    private final List<Participant> participants = new ArrayList<>();

    public Party(String name, int capacity) {
        this.name = name;
        this.capacity = capacity;
    }

    public boolean isFull() {
        return participants.size() >= capacity;
    }
}
