package com.workflow.dto;

import com.workflow.entity.triggerConditionTypes.*;
import lombok.Data;

@Data
public class TriggerRequestDto {

    private Long triggerFieldId;

    private ConditionOnTrigger triggerCondition;

    private NumberTrigger numberTrigger;

    private StringTrigger stringTrigger;

    private DateTrigger dateTrigger;

    private StageTrigger stageTrigger;

    private UserTrigger userTrigger;

}
