package com.workflow.controller;
import com.workflow.customException.ProjectNotFoundException;
import com.workflow.customException.RuleNotFoundException;
import com.workflow.dto.RuleRequestDTO;
import com.workflow.entity.CustomResponseEntity;
import com.workflow.entity.Rule;
import com.workflow.service.RuleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController
@RequestMapping("/projects")
public class RuleController {
    @Autowired
    private RuleService ruleService;
    private final Logger logger = LoggerFactory.getLogger(RuleController.class);
    @GetMapping("/{projectId}/rules")
    public ResponseEntity<?> getAllRules(@PathVariable Long projectId) {
        try {
            List<Rule> rules = ruleService.getAllRule(projectId);
            logger.info("Retrieved a list of all rules for Project with ID: " + projectId);
            return ResponseEntity.ok(
                    new CustomResponseEntity<>("List of all rules for Project with ID: " + projectId, 200, rules)
            );
        } catch (ProjectNotFoundException ex) {
            logger.error("Project with ID {} not found", projectId);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new CustomResponseEntity("Project not found", HttpStatus.INTERNAL_SERVER_ERROR.value(), null));
        } catch (Exception ex) {
            logger.error("Error occurred while getting all the rules for Project with ID {}", projectId);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new CustomResponseEntity("Error occurred while getting all the rules", HttpStatus.INTERNAL_SERVER_ERROR.value(), null));
        }
    }
    @GetMapping("/{projectId}/rules/{ruleId}")
    public ResponseEntity<?> getRuleById(@PathVariable Long projectId, @PathVariable Long ruleId) {
        try {
            Rule rule = ruleService.getRuleById(projectId, ruleId);
            logger.info("Retrieved rule with ID: " + ruleId);
            return ResponseEntity.ok(
                    new CustomResponseEntity("Rule with ID " + ruleId, 200, rule)
            );
        } catch (ProjectNotFoundException ex) {
            logger.error("Project with ID {} not found", projectId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new CustomResponseEntity("Project not found", HttpStatus.NOT_FOUND.value(), null));
        } catch (RuleNotFoundException ex) {
            logger.error("Rule not found with ID {} for Project with ID {}", ruleId, projectId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new CustomResponseEntity("Rule not found", 404, null)
            );
        } catch (Exception ex) {
            logger.error("Error occurred while getting rule with ID {} for Project with ID {}", ruleId, projectId);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new CustomResponseEntity("Error occurred while getting rule", HttpStatus.INTERNAL_SERVER_ERROR.value(), null));
        }
    }
    @PostMapping("/{projectId}/rules")
    public ResponseEntity<?> addRule(@PathVariable Long projectId, @RequestBody RuleRequestDTO addRule) {
        try {
            Rule addedRule = ruleService.addRules(projectId, addRule);
            logger.info("Rule added successfully for Project with ID: " + projectId);
            return ResponseEntity.status(HttpStatus.CREATED).body(
                    new CustomResponseEntity("Rule created successfully", 201, addedRule)
            );
        } catch (ProjectNotFoundException ex) {
            logger.error("Project with ID {} not found", projectId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new CustomResponseEntity("Project not found", HttpStatus.NOT_FOUND.value(), null));
        } catch (Exception ex) {
            logger.error("Error occurred while adding rule for Project with ID {}", projectId);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new CustomResponseEntity("Error occurred while adding rule", HttpStatus.INTERNAL_SERVER_ERROR.value(), null));
        }
    }
    @DeleteMapping("/{projectId}/rules/{ruleId}")
    public ResponseEntity<?> deleteRule(@PathVariable Long projectId, @PathVariable Long ruleId) {
        try {
            ruleService.deleteRule(projectId, ruleId);
            logger.info("Rule with ID {} deleted successfully for Project with ID:{} ", ruleId, projectId);
            return ResponseEntity.status(HttpStatus.NO_CONTENT)
                    .body(new CustomResponseEntity("Rule deleted successfully", HttpStatus.NO_CONTENT.value(), null));
        } catch (ProjectNotFoundException ex) {
            logger.error("Project with ID {} not found", projectId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new CustomResponseEntity("Project not found", HttpStatus.NOT_FOUND.value(), null));
        } catch (RuleNotFoundException ex) {
            logger.error("Rule not found with ID {} for Project with ID {}", ruleId, projectId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new CustomResponseEntity("Rule not found", 404, null)
            );
        } catch (Exception e) {
            logger.error("Error occurred while deleting rule with ID {} for Project with ID {}", ruleId, projectId);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new CustomResponseEntity("Error occurred while deleting rule", HttpStatus.INTERNAL_SERVER_ERROR.value(), null));
        }
    }
}