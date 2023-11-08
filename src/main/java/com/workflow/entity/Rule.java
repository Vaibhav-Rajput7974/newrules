package com.workflow.entity;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.workflow.entity.actionConditionType.ActionCondition;
import com.workflow.entity.triggerConditionTypes.TriggerConditions;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Rule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ruleId;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "trigger_field_id")
    private Field triggerField;

    @OneToOne(cascade = CascadeType.ALL)
    private TriggerConditions trigger;

    @OneToOne(cascade = CascadeType.ALL)
    private ActionCondition action;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "action_field_id")
    private Field actionField;

    @ManyToOne
    @JoinColumn(name = "project_id")
    @JsonBackReference
    private Project project;
}
