package com.workflow.service;

import com.workflow.customException.ProjectNotFoundException;
import com.workflow.customException.StageNotFoundException;
import com.workflow.entity.Project;
import com.workflow.entity.Stage;
import com.workflow.repository.ProjectRepo;
import com.workflow.repository.StageRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StageService {
  private static final Logger logger = LoggerFactory.getLogger(StageService.class);

  @Autowired private StageRepo stageRepo;

  @Autowired private ProjectRepo projectRepo;

  /**
   * Get all stages for a project.
   *
   * @param projectId The ID of the project.
   * @return List of stages in the project.
   */
  public List<Stage> getAllStages(long projectId) {
    try {
      Optional<Project> projectOptional = projectRepo.findById(projectId);
      if (projectOptional.isPresent()) {
        logger.info("List of Stages in the project with ID {}", projectId);
        return projectOptional.get().getStageList();
      } else {
        throw new ProjectNotFoundException("Project with ID " + projectId + " not found");
      }
    } catch (Exception e) {
      logger.error(
          "Error occurred while retrieving stages for project with ID {}: {}",
          projectId,
          e.getMessage());
      throw e;
    }
  }

  /**
   * Get a stage by its ID for a project.
   *
   * @param projectId The ID of the project.
   * @param stageId The ID of the stage.
   * @return The stage with the given ID.
   */
  public Stage getStageById(long projectId, long stageId) {
    try {
      Optional<Project> projectOptional = projectRepo.findById(projectId);
      if (projectOptional.isPresent()) {
        Project project = projectOptional.get();
        Optional<Stage> stageOptional =
            project.getStageList().stream()
                .filter(stage -> stage.getStageId().equals(stageId))
                .findFirst();
        if (stageOptional.isPresent()) {
          logger.info("Stage with ID {} found in project with ID {}", stageId, projectId);
          return stageOptional.get();
        } else {
          throw new StageNotFoundException("Stage with ID " + stageId + " not found");
        }
      } else {
        throw new ProjectNotFoundException("Project with ID " + projectId + " not found");
      }
    } catch (Exception e) {
      logger.error(
          "Error occurred while retrieving stage with ID {} in project with ID {}: {}",
          stageId,
          projectId,
          e.getMessage());
      throw e;
    }
  }

  /**
   * Add a new stage to a project.
   *
   * @param projectId The ID of the project.
   * @param stage The stage to be added.
   * @return The added stage.
   */
  public Stage addNewStage(long projectId, Stage stage) {
    try {
      Optional<Project> projectOptional = projectRepo.findById(projectId);
      if (projectOptional.isPresent()) {
        Project project = projectOptional.get();
        stage.setProject(project);
        project.getStageList().add(stage);
        projectRepo.save(project);
        logger.info("Stage added successfully");
        return stageRepo.save(stage);
      } else {
        throw new ProjectNotFoundException("Project with ID " + projectId + " not found");
      }
    } catch (Exception e) {
      logger.error(
          "Error occurred while adding a new stage to project with ID {}: {}",
          projectId,
          e.getMessage());
      throw e;
    }
  }

  /**
   * Update a stage in a project.
   *
   * @param projectId The ID of the project.
   * @param updateStage The updated stage.
   * @return The updated stage.
   */
  public Stage updateStage(long projectId, Stage updateStage) {
    try {
      Optional<Project> projectOptional = projectRepo.findById(projectId);
      if (projectOptional.isPresent()) {
        Optional<Stage> stageOptional = stageRepo.findById(updateStage.getStageId());
        if (stageOptional.isPresent()) {
          Stage existingStage = stageOptional.get();
          existingStage.setStageName(updateStage.getStageName());
          existingStage.setStageDescription(updateStage.getStageDescription());
          logger.info("Stage updated successfully");
          return stageRepo.save(existingStage);
        } else {
          throw new StageNotFoundException(
              "Stage with ID " + updateStage.getStageId() + " not found");
        }
      } else {
        throw new ProjectNotFoundException("Project with ID " + projectId + " not found");
      }
    } catch (Exception e) {
      logger.error(
          "Error occurred while updating stage in project with ID {}: {}",
          projectId,
          e.getMessage());
      throw e;
    }
  }

  /**
   * Delete a stage from a project.
   *
   * @param projectId The ID of the project.
   * @param stageId The ID of the stage to be deleted.
   */
  public void deleteStageById(long projectId, long stageId) {
    try {
      Optional<Project> projectOptional = projectRepo.findById(projectId);
      if (projectOptional.isPresent()) {
        Optional<Stage> stageOptional = stageRepo.findById(stageId);
        if (stageOptional.isPresent()) {
          stageRepo.deleteById(stageId);

          logger.info("Stage deleted successfully");
        } else {
          throw new StageNotFoundException("Stage with ID " + stageId + " not found");
        }
      } else {
        throw new ProjectNotFoundException("Project with ID " + projectId + " not found");
      }
    } catch (Exception e) {
      logger.error(
          "Error occurred while deleting stage in project with ID {}: {}",
          projectId,
          e.getMessage());
      throw e;
    }
  }

  public void reorderStage(long projectId, int sourceIndex, int destinationIndex) {
    Optional<Project> projectOptional = projectRepo.findById(projectId);
    if (projectOptional.isPresent()) {
      Project project = projectOptional.get();
      List<Stage> stageList = project.getStageList();

      Stage stageToMove = stageList.remove(sourceIndex);
      stageList.add(destinationIndex, stageToMove);
      projectRepo.save(project);
    }
  }
}
