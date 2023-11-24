package com.workflow.dto;


import com.workflow.entity.actionConditionType.*;
import com.workflow.entity.triggerConditionTypes.*;
import lombok.Data;

import java.util.List;

@Data
public class RuleRequestDTO {

    private List<TriggerRequestDto> triggerRequestDtos;

    private List<ActionRequestDto> actionRequestDtos;

}