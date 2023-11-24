package com.workflow.service;

import com.workflow.customException.ProjectNotFoundException;
import com.workflow.customException.RuleNotFoundException;
import com.workflow.dto.RuleRequestDTO;
import com.workflow.entity.*;
import com.workflow.entity.actionConditionType.*;
import com.workflow.entity.triggerConditionTypes.*;
import com.workflow.repository.FieldRepo;
import com.workflow.repository.ProjectRepo;
import com.workflow.repository.RuleRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class RuleService {
  @Autowired private ProjectRepo projectRepo;
  @Autowired private RuleRepo ruleRepo;

  @Autowired private FieldRepo fieldRepo;

  private static final Logger logger = LoggerFactory.getLogger(RuleService.class);

  public List<Rule> getAllRule(long projectId) {
    try {
      Optional<Project> projectOptional = projectRepo.findById(projectId);
      if (projectOptional.isPresent()) {
        logger.info("Fetched rules for project with ID: {}", projectId);
        return projectOptional.get().getRulesList();
      } else {
        logger.error("Project not found with ID: {}", projectId);
        throw new ProjectNotFoundException("Project with ID " + projectId + " not found");
      }
    } catch (Exception e) {
      logger.error(
          "Error occurred while fetching rules for project with ID {}: {}",
          projectId,
          e.getMessage());
      throw e;
    }
  }

  public Rule getRuleById(long projectID, long ruleID) {
    try {
      Optional<Project> projectOptional = projectRepo.findById(projectID);
      if (projectOptional.isPresent()) {
        Project project = projectOptional.get();
        Optional<Rule> ruleOptional =
            project.getRulesList().stream().filter(rule -> rule.getRuleId() == ruleID).findFirst();
        if (ruleOptional.isPresent()) {
          logger.info("Fetched rule with ID {} for project with ID: {}", ruleID, projectID);
          return ruleOptional.get();
        } else {
          logger.error("Rule not found with ID {} for project with ID: {}", ruleID, projectID);
          throw new RuleNotFoundException("Rule with ID " + ruleID + " not found");
        }
      } else {
        logger.error("Project not found with ID: {}", projectID);
        throw new ProjectNotFoundException("Project with ID " + projectID + " not found");
      }
    } catch (Exception e) {
      logger.error(
          "Error occurred while fetching rule with ID {} for project with ID {}: {}",
          ruleID,
          projectID,
          e.getMessage());
      throw e;
    }
  }

  public Rule addRules(long projectId, RuleRequestDTO addRule) {
    try {
      Rule rule = new Rule();
      Optional<Project> projectOptional = projectRepo.findById(projectId);
      if (projectOptional.isPresent()) {
        Project project = projectOptional.get();
        rule.setProject(project);
        addRule
            .getTriggerRequestDtos()
            .forEach(
                (trigger) -> {
                  Trigger newTrigger = new Trigger();
                  System.out.println(trigger.getTriggerFieldId());

                  Optional<Field> fieldOptionalTrigger =
                      fieldRepo.findById(trigger.getTriggerFieldId());
                  Field fieldTrigger = fieldOptionalTrigger.get();

                  newTrigger.setTriggerField(fieldTrigger);
                  if (trigger.getTriggerCondition() == ConditionOnTrigger.STRING) {
                    StringTrigger stringTrigger = trigger.getStringTrigger();
                    newTrigger.setTriggerConditions(stringTrigger);
                  } else if (trigger.getTriggerCondition() == ConditionOnTrigger.NUMBER) {
                    NumberTrigger numberTrigger = trigger.getNumberTrigger();
                    newTrigger.setTriggerConditions(numberTrigger);
                  } else if (trigger.getTriggerCondition() == ConditionOnTrigger.DATE) {
                    DateTrigger dateTrigger = trigger.getDateTrigger();
                    newTrigger.setTriggerConditions(dateTrigger);
                  } else if (trigger.getTriggerCondition() == ConditionOnTrigger.STAGE) {
                    StageTrigger stageTrigger = trigger.getStageTrigger();
                    newTrigger.setTriggerConditions(stageTrigger);
                  } else if (trigger.getTriggerCondition() == ConditionOnTrigger.USER) {
                    UserTrigger userTrigger = trigger.getUserTrigger();
                    newTrigger.setTriggerConditions(userTrigger);
                  }
                  newTrigger.setRule(rule);
                  rule.getTriggerList().add(newTrigger);
                });
        actionSelect(addRule, rule);
        logger.info("Created rules for project with ID: {}", projectId);
        return ruleRepo.save(rule);
      } else {
        logger.error("Project not found with ID: {}", projectId);
        throw new ProjectNotFoundException("Project with ID " + projectId + " not found");
      }

      //
    } catch (Exception e) {
      logger.error(
          "Error occurred while creating rules for project with ID {}: {} {}",
          projectId,
          e.getMessage(),
          e);
      throw e;
    }
  }

  public void actionSelect(RuleRequestDTO addRule, Rule rule) {
    addRule
        .getActionRequestDtos()
        .forEach(
            action -> {
              Action newAction = new Action();
              System.out.println(action.getActionFieldId());

              Optional<Field> fieldOptionalAction = fieldRepo.findById(action.getActionFieldId());
              Field fieldaAction = fieldOptionalAction.get();
              newAction.setActionField(fieldaAction);
              if (action.getActionCondition() == ConditionOnAction.STAGE) {
                StageAction stageAction = action.getStageAction();
                newAction.setActionCondition(stageAction);
              } else if (action.getActionCondition() == ConditionOnAction.NUMBER) {
                NumberAction numberAction = action.getNumberAction();
                newAction.setActionCondition(numberAction);
              } else if (action.getActionCondition() == ConditionOnAction.STRING) {
                StringAction stringAction = action.getStringAction();
                newAction.setActionCondition(stringAction);
              } else if (action.getActionCondition() == ConditionOnAction.PROJECT) {
                ProjectAction projectAction = action.getProjectAction();
                newAction.setActionCondition(projectAction);
              } else if (action.getActionCondition() == ConditionOnAction.DATE) {
                DateAction dateAction = action.getDateAction();
                newAction.setActionCondition(dateAction);
              } else if (action.getActionCondition() == ConditionOnAction.USER) {
                UserAction userAction = action.getUserAction();
                newAction.setActionCondition(userAction);
              }
              newAction.setRule(rule);
              rule.getActionList().add(newAction);
            });
  }

  public void deleteRule(Long projectId, Long ruleId) {
    Project project =
        projectRepo
            .findById(projectId)
            .orElseThrow(
                () -> new ProjectNotFoundException("Project not found with ID: " + projectId));
    Rule rule =
        project.getRulesList().stream()
            .filter(r -> r.getRuleId().equals(ruleId))
            .findFirst()
            .orElseThrow(() -> new RuleNotFoundException("Rule not found with ID: " + ruleId));
    project.getRulesList().remove(rule);
    try {
      projectRepo.save(project);
      ruleRepo.delete(rule);
      logger.info("Deleted rule with ID {} in project with ID: {}", ruleId, projectId);
    } catch (Exception e) {
      logger.error(
          "Error occurred while deleting rule with Id {} in project with ID {}: {}",
          ruleId,
          projectId,
          e.getMessage());
      throw e;
    }
  }
}
