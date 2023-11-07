package com.workflow.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long projectId;

    @NotBlank(message = "Project Name Cannot Be Blank")
    private String projectName;

    private String projectDescription;

    @OrderColumn(name = "stage_order")
    @JsonManagedReference
    @OneToMany(mappedBy = "project" , cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    private List<Stage> stageList=new ArrayList<>();

    @JsonManagedReference
    @OneToMany(mappedBy = "project" , cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    private List<Rule> rulesList;

    private List<String> status = new ArrayList<>();
}