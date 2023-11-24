package com.workflow.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.workflow.entity.triggerConditionTypes.TriggerConditions;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "triggers")
public class Trigger {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long triggerId;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "trigger_field_id")
    private Field triggerField;

    @OneToOne(cascade = CascadeType.ALL)
    private TriggerConditions triggerConditions;

    @ManyToOne(cascade = CascadeType.ALL)
    @JsonBackReference
    private  Rule rule;


}
