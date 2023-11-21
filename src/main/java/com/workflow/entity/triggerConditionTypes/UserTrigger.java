package com.workflow.entity.triggerConditionTypes;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class UserTrigger extends TriggerConditions{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String operation;

    private Long previous;

    private Long current;



    @Override
    public ConditionOnTrigger getConditionType() {
        return ConditionOnTrigger.USER;
    }
}
