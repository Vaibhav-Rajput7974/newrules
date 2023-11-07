package com.workflow.entity.triggerConditionTypes;

import jakarta.persistence.*;

@Entity
@Table(name = "trigger_condition")
public abstract class TriggerConditions {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public abstract ConditionOnTrigger getConditionType();
}
