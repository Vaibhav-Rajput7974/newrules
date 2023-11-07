package com.workflow.entity.triggerConditionTypes;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
public class NumberTrigger extends TriggerConditions {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String operation;

    private Long value;

    @Override
    public ConditionOnTrigger getConditionType() {
        return ConditionOnTrigger.NUMBER;
    }
}
