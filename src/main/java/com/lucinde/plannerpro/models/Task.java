package com.lucinde.plannerpro.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name="tasks")
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String description;
    private String workPerformed;
    private Boolean jobDone;

    @ManyToOne
    @JsonIgnore
    private Customer customer;

    @OneToMany(mappedBy = "task", orphanRemoval = true)
    private List<ScheduleTask> scheduleTaskList;

    @OneToMany(mappedBy = "task", orphanRemoval = true)
    @JsonIgnore
    private List<File> fileList;

}
