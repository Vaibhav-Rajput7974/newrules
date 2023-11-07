package com.workflow.entity.actionConditionType;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class NumberAction extends ActionCondition{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String operation;

    private int number;

    @Override
    public ConditionOnAction getConditionType() {
        return ConditionOnAction.NUMBER;
    }
}
