package com.workflow.entity.triggerConditionTypes;

import jakarta.persistence.*;

@Entity
@Table(name = "trigger_condition")
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "condition_type")
public abstract class TriggerConditions {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public abstract ConditionOnTrigger getConditionType();
}
