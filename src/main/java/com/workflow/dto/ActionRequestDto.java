package com.workflow.dto;

import com.workflow.entity.actionConditionType.*;
import lombok.Data;

@Data
public class ActionRequestDto {

    private Long actionFieldId;

    private ConditionOnAction actionCondition;

    private StageAction stageAction;

    private NumberAction numberAction;

    private StringAction stringAction;

    private DateAction dateAction;

    private ProjectAction projectAction;

    private UserAction userAction;
}
