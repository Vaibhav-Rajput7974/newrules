package com.workflow.dto;


import com.workflow.entity.actionConditionType.*;
import com.workflow.entity.triggerConditionTypes.*;
import lombok.Data;

@Data
public class RuleRequestDTO {

    private Long triggerFieldId;

    private ConditionOnTrigger triggerCondition;

    private NumberTrigger numberTrigger;

    private StringTrigger stringTrigger;

    private DateTrigger dateTrigger;

    private StageTrigger stageTrigger;

    private UserTrigger userTrigger;

    private Long actionFieldId;

    private ConditionOnAction actionCondition;

    private StageAction stageAction;

    private NumberAction numberAction;

    private StringAction stringAction;

    private DateAction dateAction;

    private ProjectAction projectAction;

    private UserAction userAction;
}