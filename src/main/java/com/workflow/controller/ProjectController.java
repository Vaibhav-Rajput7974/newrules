package com.workflow.controller;

import com.workflow.customException.ProjectNotFoundException;
import com.workflow.dto.ProjectDto;
import com.workflow.entity.CustomResponseEntity;
import com.workflow.entity.Field;
import com.workflow.entity.Project;
import com.workflow.entity.Stage;
import com.workflow.service.ProjectService;
import com.workflow.service.StageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/projects")
public class ProjectController {

    private static final Logger logger = LoggerFactory.getLogger(ProjectController.class);

    @Autowired
    private ProjectService projectService;

    @Autowired
    private StageService stageService;

    /**
     * Get a list of all projects.
     * @return ResponseEntity containing the list of projects and a status code.
     */
    @GetMapping
    public ResponseEntity<?> getAllProjects() {
        try {
            List<Project> projects = projectService.getAllProjects();
            logger.info("Retrieved all projects");
            return ResponseEntity.ok(new CustomResponseEntity<>("All projects retrieved successfully", 200, projects));
        } catch (Exception e) {
            logger.error("Error occurred while retrieving projects {}",e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new CustomResponseEntity("Error occurred while retrieving projects", 500, null));
        }
    }

    /**
     * Get a project by its ID.
     * @param projectId The ID of the project to retrieve.
     * @return ResponseEntity containing the project and a status code.
     */
    @GetMapping("/{projectId}")
    public ResponseEntity<?> getProjectById(@PathVariable long projectId) {
        try {
            Project project = projectService.getProjectById(projectId);
            logger.info("Project with ID {} found", projectId);
            return ResponseEntity.ok(new CustomResponseEntity("Project with ID found successfully", 200, project));
        } catch (ProjectNotFoundException ex) {
            logger.error("Project with ID {} not found", projectId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new CustomResponseEntity("Project with ID not found", 404, null));
        } catch (Exception ex) {
            logger.error("Error occurred while retrieving project with ID {} {}", projectId,ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new CustomResponseEntity("Error occurred while retrieving project", 500, null));
        }
    }

    /**
     * Add a new project along with default stages.
     * @param addProject The project to add.
     * @return ResponseEntity containing the added project and a status code.
     */
    @PostMapping
    public ResponseEntity<?> addNewProject(@RequestBody Project addProject) {
        try {
            System.out.println("DONE");
            Project savedProject = projectService.addNewProject(addProject);

            ProjectDto projectDto = new ProjectDto();
            projectDto.setProjectId(addProject.getProjectId());
            projectDto.setProjectName(addProject.getProjectName());
            projectDto.setProjectDescription(addProject.getProjectDescription());

            // Create and add the default stages
            Stage stage1 = new Stage("To-Do");
            Stage stage2 = new Stage("Doing");
            Stage stage3 = new Stage("Done");

            savedProject.getStatus().add("to-Do");
            savedProject.getStatus().add("progress");
            savedProject.getStatus().add("done");

            Stage savedStage1=stageService.addNewStage(savedProject.getProjectId(), stage1);
            Stage savedStage2=stageService.addNewStage(savedProject.getProjectId(), stage2);
            Stage savedStage3=stageService.addNewStage(savedProject.getProjectId(), stage3);

            Project savedProject2=projectService.addNewProject(savedProject);

            logger.info("Project added successfully with default stages");
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new CustomResponseEntity("Project added successfully with default stages", 201, savedProject2));
        } catch (Exception ex) {
            logger.error("Error occurred while adding a project {}",ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new CustomResponseEntity("Project Name Can't Be Null, Please Provide Valid Input.", 500, null));
        }
    }

    /**
     * Delete a project by its ID.
     * @param projectId The ID of the project to delete.
     * @return ResponseEntity with a status code.
     */
    @DeleteMapping("/{projectId}")
    public ResponseEntity<?> deleteProjectById(@PathVariable long projectId) {
        try {
            projectService.deleteProjectById(projectId);
            logger.info("Project with ID {} deleted successfully", projectId);
            return ResponseEntity.status(HttpStatus.NO_CONTENT)
                    .body(new CustomResponseEntity("Project with ID deleted successfully", HttpStatus.NO_CONTENT.value(), null));
        } catch (ProjectNotFoundException ex) {
            logger.error("Project with ID {} not found", projectId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new CustomResponseEntity("Project with ID not found", HttpStatus.NOT_FOUND.value(), null));
        } catch (Exception ex) {
            logger.error("Error occurred while deleting project with ID {}", projectId);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new CustomResponseEntity("Error occurred while deleting project", HttpStatus.INTERNAL_SERVER_ERROR.value(), null));
        }
    }
    @GetMapping("/fields")
    public ResponseEntity<?> getField() {
        try {
            List<Field> fieldList = projectService.getAllField();
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new CustomResponseEntity("All fields retrieved successfully", HttpStatus.NO_CONTENT.value(), fieldList));
        } catch (ProjectNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new CustomResponseEntity("Fields with ID not found", HttpStatus.NOT_FOUND.value(), null));
        }
    }
}
