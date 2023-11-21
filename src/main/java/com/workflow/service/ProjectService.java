package com.workflow.service;

import com.workflow.customException.ProjectNotFoundException;
import com.workflow.entity.Field;
import com.workflow.entity.Project;
import com.workflow.entity.Rule;
import com.workflow.entity.triggerConditionTypes.ConditionOnTrigger;
import com.workflow.entity.triggerConditionTypes.DateTrigger;
import com.workflow.repository.FieldRepo;
import com.workflow.repository.ProjectRepo;
import com.workflow.repository.RuleRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class ProjectService {

    private static final Logger logger = LoggerFactory.getLogger(ProjectService.class);

    @Autowired
    private ProjectRepo projectRepo;

    @Autowired
    private TriggerStart triggerStart;

    @Autowired
    private RuleRepo ruleRepo;

    @Autowired
    private FieldRepo fieldRepo;


    /**
     * Retrieve a list of all projects.
     *
     * @return List of all projects.
     * @throws Exception if an error occurs during the operation.
     */
    public List<Project> getAllProjects() {
        try {
            logger.info("Retrieving all projects");
            return projectRepo.findAll();
        } catch (Exception e) {
            logger.error("Error occurred while retrieving projects: {}", e.getMessage(), e);
            throw e;
        }
    }

    /**
     * Retrieve a project by its unique ID.
     *
     * @param projectId The ID of the project to retrieve.
     * @return The project with the given ID.
     * @throws ProjectNotFoundException if the project with the specified ID is not found.
     * @throws Exception if an error occurs during the operation.
     */
    public Project getProjectById(long projectId) {
        try {
            Optional<Project> project = projectRepo.findById(projectId);
            if (project.isPresent()) {
                logger.info("Retrieving project with ID = " + projectId);
                return project.get();
            } else {
                throw new ProjectNotFoundException("Project with ID " + projectId + " not found");
            }
        } catch (Exception e) {
            logger.error("Error occurred while retrieving project by ID {}: {}", projectId, e.getMessage());
            throw e;
        }
    }

    /**
     * Add a new project.
     *
     * @param newProject The project to be added.
     * @return The newly added project.
     * @throws Exception if an error occurs during the operation.
     */
    public Project addNewProject(Project newProject) {
        try {
            logger.info("Adding a new project");
            return projectRepo.save(newProject);
        } catch (Exception e) {
            logger.error("Error occurred while adding a new project: {}", e.getMessage(), e);
            throw e;
        }
    }

    /**
     * Delete a project by its unique ID.
     *
     * @param projectId The ID of the project to be deleted.
     * @return true if the project is successfully deleted; false if the project is not found.
     * @throws ProjectNotFoundException if the project with the specified ID is not found.
     * @throws Exception if an error occurs during the operation.
     */
    public boolean deleteProjectById(long projectId) {
        try {
            if (projectRepo.existsById(projectId)) {
                logger.info("Deleting project with ID = " + projectId);
                projectRepo.deleteById(projectId);
                return true;
            } else {
                throw new ProjectNotFoundException("Project with ID " + projectId + " not found");
            }
        } catch (Exception e) {
            logger.error("Error occurred while deleting project by ID {}: {}", projectId, e.getMessage(), e);
            throw e;
        }
    }

    @Scheduled(fixedRate = 60000)
    public void task1() {
        Field field = fieldRepo.findByDataType("DATE");
        List<Rule> ruleList = ruleRepo.findByTriggerField(field);
        if(!ruleList.isEmpty()){
            ruleList.forEach(rule-> {
                if( rule.getTrigger() != null &&  rule.getTrigger().getConditionType() == ConditionOnTrigger.DATE){
                    Date currentDate=new Date();
                    DateTrigger dateTrigger = (DateTrigger) rule.getTrigger();
                    try {
                        triggerStart.triggerOnDate(dateTrigger,rule,rule.getProject().getProjectId());
                    } catch (InvocationTargetException e) {
                        throw new RuntimeException(e);
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException(e);
                    } catch (NoSuchMethodException e) {
                        throw new RuntimeException(e);
                    }
                    System.out.println("Task 1 executed at: " + currentDate);
                }
            });
        }
    }
    public List<Field> getAllField() {
        try {
            return fieldRepo.findAll();
        } catch (Exception e) {
            throw e;
        }
    }

}
