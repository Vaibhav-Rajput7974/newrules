package com.workflow.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.workflow.entity.actionConditionType.ActionCondition;
import com.workflow.entity.triggerConditionTypes.TriggerConditions;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "actions")
public class Action {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long actionId;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "action_field_id")
    private Field actionField;

    @OneToOne(cascade = CascadeType.ALL)
    private ActionCondition actionCondition;

    @ManyToOne(cascade = CascadeType.ALL)
    @JsonBackReference
    private Rule rule;

}
