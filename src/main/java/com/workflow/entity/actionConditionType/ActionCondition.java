package com.workflow.entity.actionConditionType;

import jakarta.persistence.*;

@Entity
@Table(name = "action_condition")
public abstract class ActionCondition {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public abstract ConditionOnAction getConditionType();
}
