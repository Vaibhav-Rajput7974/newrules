package com.workflow.controller;

import com.workflow.customException.ProjectNotFoundException;
import com.workflow.customException.StageNotFoundException;
import com.workflow.dto.StageDto;
import com.workflow.entity.CustomResponseEntity;
import com.workflow.entity.Stage;
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
public class StageController {

    @Autowired
    private StageService stageService;

    private static final Logger logger = LoggerFactory.getLogger(StageController.class);

    /**
     * Get all stages for a project.
     *
     * @param projectId The ID of the project.
     * @return ResponseEntity containing the list of stages and a status code.
     */
    @GetMapping("/{projectId}/stages")
    public ResponseEntity<?> getAllStages(@PathVariable Long projectId) {
        try {
            List<Stage> stages = stageService.getAllStages(projectId);
            logger.info("All Stages in Project With ID {} Found", projectId);
            return ResponseEntity.ok(
                    new CustomResponseEntity<>("List of Stages in Project with ID " + projectId, HttpStatus.OK.value(), stages)
            );
        } catch (ProjectNotFoundException ex) {
            logger.error("Project with ID {} Not Found", projectId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new CustomResponseEntity("Stages in Project with ID " + projectId + " Not Found", HttpStatus.NOT_FOUND.value(), null)
            );
        } catch (Exception ex) {
            logger.error("Error occurred while retrieving stages for Project with ID {}", projectId);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new CustomResponseEntity("Error occurred while retrieving stages", HttpStatus.INTERNAL_SERVER_ERROR.value(), null));
        }
    }

    /**
     * Get a stage by its ID for a project.
     *
     * @param projectId The ID of the project.
     * @param stageId   The ID of the stage.
     * @return ResponseEntity containing the stage and a status code.
     */
    @GetMapping("/{projectId}/stages/{stageId}")
    public ResponseEntity<?> getStageById(@PathVariable long projectId, @PathVariable long stageId) {
        try {
            Stage stage = stageService.getStageById(projectId, stageId);
            logger.info("Stage with ID {} Found in Project with ID {}", stageId, projectId);
            return ResponseEntity.ok(
                    new CustomResponseEntity("Stage with ID Found", HttpStatus.OK.value(), stage)
            );
        } catch (ProjectNotFoundException ex) {
            logger.error("Project with ID {} Not Found", projectId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new CustomResponseEntity("Stage in Project with ID " + projectId + " Not Found", HttpStatus.NOT_FOUND.value(), null)
            );
        } catch (StageNotFoundException ex) {
            logger.error("Stage with ID {} Not Found", stageId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new CustomResponseEntity("Stage with ID " + stageId + " Not Found", HttpStatus.NOT_FOUND.value(), null)
            );
        } catch (Exception ex) {
            logger.error("Error occurred while retrieving stage");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new CustomResponseEntity("Error occurred while retrieving stage", HttpStatus.INTERNAL_SERVER_ERROR.value(), null));
        }
    }

    /**
     * Add a new stage to a project.
     *
     * @param projectId The ID of the project.
     * @param addStage  The stage to be added.
     * @return ResponseEntity containing the added stage and a status code.
     */
    @PostMapping("/{projectId}/stages")
    public ResponseEntity<?> addNewStage(@PathVariable long projectId, @RequestBody Stage addStage) {
        try {
            Stage savedStage = stageService.addNewStage(projectId, addStage);
            logger.info("Stage Added Successfully");

            StageDto stageDto = new StageDto();
            stageDto.setStageId(addStage.getStageId());
            stageDto.setStageName(addStage.getStageName());
            stageDto.setStageDescription(addStage.getStageDescription());

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new CustomResponseEntity("Stage Added Successfully", HttpStatus.CREATED.value(), stageDto));
        } catch (ProjectNotFoundException ex) {
            logger.error("Project with ID {} Not Found", projectId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new CustomResponseEntity("Project with ID " + projectId + " Not Found", HttpStatus.NOT_FOUND.value(), null)
            );
        } catch (Exception ex) {
            logger.error("Error occurred while adding stage");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new CustomResponseEntity("Stage Name Can't Be Null, Please Provide Valid Input.", HttpStatus.INTERNAL_SERVER_ERROR.value(), null));
        }
    }

    /**
     * Update a stage in a project.
     *
     * @param projectId   The ID of the project.
     * @param updateStage The updated stage.
     * @return ResponseEntity containing the updated stage and a status code.
     */
    @PutMapping("/{projectId}/stages")
    public ResponseEntity<?> updateStageById(@PathVariable long projectId, @RequestBody Stage updateStage) {
        try {
            Stage updatedStage = stageService.updateStage(projectId, updateStage);
            logger.info("Stage Updated Successfully");
            return ResponseEntity.ok(
                    new CustomResponseEntity("Stage Updated Successfully", HttpStatus.OK.value(), updatedStage)
            );
        } catch (ProjectNotFoundException ex) {
            logger.error("Project with ID {} Not Found", projectId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new CustomResponseEntity("Project with ID " + projectId + " Not Found", HttpStatus.NOT_FOUND.value(), null)
            );
        } catch (StageNotFoundException ex) {
            logger.error("Stage with ID {} Not Found", updateStage.getStageId());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new CustomResponseEntity("Stage with ID " + updateStage.getStageId() + " Not Found", HttpStatus.NOT_FOUND.value(), null)
            );
        } catch (Exception ex) {
            logger.error("Error occurred while updating stage");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new CustomResponseEntity("Error occurred while updating stage", HttpStatus.INTERNAL_SERVER_ERROR.value(), null));
        }
    }

    /**
     * Delete a stage from a project.
     *
     * @param projectId The ID of the project.
     * @param stageId   The ID of the stage to be deleted.
     * @return ResponseEntity with a status code (204 for successful deletion).
     */
    @DeleteMapping("/{projectId}/stages/{stageId}")
    public ResponseEntity<?> deleteStageById(@PathVariable Long projectId, @PathVariable long stageId) {
        try {
            stageService.deleteStageById(projectId, stageId);
            logger.info("Stage Deleted Successfully");
            return ResponseEntity.status(HttpStatus.NO_CONTENT)
                    .body(new CustomResponseEntity("Stage Deleted Successfully", HttpStatus.NO_CONTENT.value(), null));
        } catch (ProjectNotFoundException ex) {
            logger.error("Project with ID {} Not Found", projectId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new CustomResponseEntity("Project with ID " + projectId + " Not Found", HttpStatus.NOT_FOUND.value(), null)
            );
        } catch (StageNotFoundException ex) {
            logger.error("Stage with ID {} Not Found", stageId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new CustomResponseEntity("Stage with ID " + stageId + " Not Found", HttpStatus.NOT_FOUND.value(), null)
            );
        } catch (Exception ex) {
            logger.error("Error occurred while deleting stage");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new CustomResponseEntity("Error occurred while deleting stage", HttpStatus.INTERNAL_SERVER_ERROR.value(), null));
        }
    }

    @PutMapping("{projectId}/changeorder/{sourceIndex}/{destinationIndex}")
    public ResponseEntity<?> reorderStage(@PathVariable Long projectId, @PathVariable int sourceIndex,@PathVariable int destinationIndex) {
        try{
            stageService.reorderStage(projectId,sourceIndex,destinationIndex);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(
                            new CustomResponseEntity<>("stage Order Changed",200,null)
                    );
        }catch (Exception ex) {
            logger.error("Error occurred while deleting stage");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new CustomResponseEntity("Error occurred while deleting stage", HttpStatus.INTERNAL_SERVER_ERROR.value(), null));
        }
    }
}
