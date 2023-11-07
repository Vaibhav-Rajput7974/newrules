package com.workflow.entity.actionConditionType;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

import java.util.Date;

@Data
@Entity
public class DateAction extends ActionCondition{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String operation;

    private Date date;
    @Override
    public ConditionOnAction getConditionType() {
        return ConditionOnAction.DATE;
    }
}
