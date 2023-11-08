package com.workflow.entity.triggerConditionTypes;

import com.workflow.entity.User;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class UserTrigger extends TriggerConditions{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String operation;

    @OneToOne
    private User userTrigger;
    @Override
    public ConditionOnTrigger getConditionType() {
        return ConditionOnTrigger.USER;
    }
}
