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
    @Autowired
    private ProjectRepo projectRepo;
    @Autowired
    private RuleRepo ruleRepo;

    @Autowired
    private FieldRepo fieldRepo;


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
            logger.error("Error occurred while fetching rules for project with ID {}: {}", projectId, e.getMessage());
            throw e;
        }
    }
    public Rule getRuleById(long projectID, long ruleID) {
        try {
            Optional<Project> projectOptional = projectRepo.findById(projectID);
            if (projectOptional.isPresent()) {
                Project project = projectOptional.get();
                Optional<Rule> ruleOptional = project.getRulesList().stream()
                        .filter(rule -> rule.getRuleId() == ruleID)
                        .findFirst();
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
            logger.error("Error occurred while fetching rule with ID {} for project with ID {}: {}", ruleID, projectID, e.getMessage());
            throw e;
        }
    }

    public Rule addRules(long projectId, RuleRequestDTO addRule) {
        try {
            System.out.println(addRule.getActionFieldId());
            System.out.println(addRule.getTriggerFieldId());
            Optional<Project> projectOptional = projectRepo.findById(projectId);
            Optional<Field> fieldOptionalTrigger=fieldRepo.findById(addRule.getTriggerFieldId());
            Optional<Field> fieldOptionalAction=fieldRepo.findById(addRule.getActionFieldId());
            Field fieldTrigger =fieldOptionalTrigger.get();
            Field fieldAction=fieldOptionalAction.get();
//            System.out.println(fieldTrigger);
//            System.out.println(fieldAction);
            if (projectOptional.isPresent()) {

                Rule rule = new Rule();
                Project project = projectOptional.get();
                rule.setProject(project);
                rule.setTriggerField(fieldTrigger);
                rule.setActionField(fieldAction);
                if(addRule.getTriggerCondition() == ConditionOnTrigger.STRING){
                    StringTrigger stringTrigger = addRule.getStringTrigger();
                    rule.setTrigger(stringTrigger);
                } else if (addRule.getTriggerCondition() == ConditionOnTrigger.NUMBER) {
                    NumberTrigger numberTrigger =addRule.getNumberTrigger();
                    rule.setTrigger(numberTrigger);
                } else if (addRule.getTriggerCondition() == ConditionOnTrigger.DATE) {
                    DateTrigger dateTrigger=addRule.getDateTrigger();
                    rule.setTrigger(dateTrigger);
                } else if (addRule.getTriggerCondition() == ConditionOnTrigger.STAGE) {
                    StageTrigger stageTrigger=addRule.getStageTrigger();
                    rule.setTrigger(stageTrigger);
                }
                else if (addRule.getTriggerCondition() == ConditionOnTrigger.USER) {
                    UserTrigger userTrigger=addRule.getUserTrigger();
                    rule.setTrigger(userTrigger);
                }
                actionSelect(addRule,rule);
                logger.info("Created rules for project with ID: {}", projectId);
                return ruleRepo.save(rule);
            } else {
                logger.error("Project not found with ID: {}", projectId);
                throw new ProjectNotFoundException("Project with ID " + projectId + " not found");
            }
        } catch (Exception e) {
            logger.error("Error occurred while creating rules for project with ID {}: {} {}", projectId, e.getMessage(),e);
            throw e;
        }
    }
    public void actionSelect(RuleRequestDTO addRule, Rule rule){
        if(addRule.getActionCondition() == ConditionOnAction.STAGE){
            StageAction stageAction = addRule.getStageAction();
            rule.setAction(stageAction);
        } else if (addRule.getActionCondition() == ConditionOnAction.NUMBER){
            NumberAction numberAction= addRule.getNumberAction();
            rule.setAction(numberAction);
        } else if (addRule.getActionCondition() == ConditionOnAction.STRING){
            StringAction stringAction= addRule.getStringAction();
            rule.setAction(stringAction);
        } else if (addRule.getActionCondition() == ConditionOnAction.PROJECT) {
            ProjectAction projectAction = addRule.getProjectAction();
            rule.setAction(projectAction);
        } else if (addRule.getActionCondition() == ConditionOnAction.DATE) {
            DateAction dateAction = addRule.getDateAction();
            rule.setAction(dateAction);
        }
        else if (addRule.getActionCondition() == ConditionOnAction.USER) {
            UserAction userAction=addRule.getUserAction();
            rule.setAction(userAction);
        }
    }
    public void deleteRule(Long projectId, Long ruleId) {
        Project project = projectRepo.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException("Project not found with ID: " + projectId));
        Rule rule = project.getRulesList().stream()
                .filter(r -> r.getRuleId().equals(ruleId))
                .findFirst()
                .orElseThrow(() -> new RuleNotFoundException("Rule not found with ID: " + ruleId));
        project.getRulesList().remove(rule);
        try {
            projectRepo.save(project);
            ruleRepo.delete(rule);
            logger.info("Deleted rule with ID {} in project with ID: {}", ruleId, projectId);
        } catch (Exception e) {
            logger.error("Error occurred while deleting rule with Id {} in project with ID {}: {}", ruleId, projectId, e.getMessage());
            throw e;
        }
    }
}