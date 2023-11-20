package com.workflow.entity.actionConditionType;

import com.workflow.entity.User;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class UserAction extends ActionCondition{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String operation;

    @JoinColumn(name = "user_Id")
    @ManyToOne
    private User userAction;

    @Override
    public ConditionOnAction getConditionType() {
        return ConditionOnAction.USER;
    }
}
