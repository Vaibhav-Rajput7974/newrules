package com.workflow.entity.actionConditionType;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
public class StringAction extends ActionCondition {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String operation;

    private String nextString;

    @Override
    public ConditionOnAction getConditionType() {
        return ConditionOnAction.STRING;
    }
}
