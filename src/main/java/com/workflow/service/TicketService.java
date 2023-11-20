package com.workflow.service;

import com.workflow.customException.ProjectNotFoundException;
import com.workflow.customException.StageNotFoundException;
import com.workflow.customException.TicketNotFoundException;
import com.workflow.dto.TicketDto;
import com.workflow.entity.Project;
import com.workflow.entity.Stage;
import com.workflow.entity.Ticket;
import com.workflow.entity.User;
import com.workflow.repository.ProjectRepo;
import com.workflow.repository.StageRepo;
import com.workflow.repository.TicketRepo;
import com.workflow.repository.UserRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Optional;

@Service
public class TicketService {
  private static final Logger logger = LoggerFactory.getLogger(TicketService.class);
  @Autowired private TicketRepo ticketRepo;
  @Autowired private ProjectRepo projectRepo;
  @Autowired private StageRepo stageRepo;
  @Autowired private UserRepo userRepo;
  @Autowired private ProjectService projectService;
  @Autowired private StageService stageService;

  @Autowired private TriggerStart triggerStart;

  public List<Ticket> getTicketsByProjectAndStage(long projectId, long stageId) {
    try {
      Optional<Project> projectOptional = projectRepo.findById(projectId);
      if (projectOptional.isPresent()) {
        Project project = projectOptional.get();
        Optional<Stage> stageOptional =
            project.getStageList().stream()
                .filter(stage -> stage.getStageId() == (stageId))
                .findFirst();
        if (stageOptional.isPresent()) {
          List<Ticket> tickets = stageOptional.get().getTicketList();
          logger.info("List Of Tickets In The Stage");
          return tickets;
        } else {
          throw new StageNotFoundException(
              "Stage with ID " + stageId + " not found for Project " + projectId);
        }
      } else {
        throw new ProjectNotFoundException("Project with ID " + projectId + " not found.");
      }
    } catch (Exception e) {
      logger.error(
          "Error occurred while retrieving tickets by Project ID {} and Stage ID {}: {}",
          projectId,
          stageId,
          e.getMessage(),
          e);
      throw e;
    }
  }

  public Optional<Ticket> getTicketById(long ticketId, long projectId, long stageId) {
    try {
      Optional<Project> projectOptional = projectRepo.findById(projectId);
      if (projectOptional.isPresent()) {
        Project project = projectOptional.get();
        Optional<Stage> stageOptional =
            project.getStageList().stream()
                .filter(stage -> stage.getStageId() == (stageId))
                .findFirst();
        if (stageOptional.isPresent()) {
          Optional<Ticket> ticket = ticketRepo.findById(ticketId);
          if (!ticket.isPresent()) {
            throw new TicketNotFoundException(
                "Ticket with ID "
                    + ticketId
                    + " not found in Project "
                    + projectId
                    + " and Stage "
                    + stageId);
          }
          logger.info("Get Ticket By Id");
          return ticket;
        } else {
          throw new StageNotFoundException(
              "Stage with ID " + stageId + " not found for Project " + projectId);
        }
      } else {
        throw new ProjectNotFoundException("Project with ID " + projectId + " not found.");
      }
    } catch (Exception e) {
      logger.error(
          "Error occurred while retrieving ticket by ID {} in Project {} and Stage {}: {}",
          ticketId,
          projectId,
          stageId,
          e.getMessage(),
          e);
      throw e;
    }
  }

  public Ticket addTicket(Ticket addTicket, long projectId, long stageId) throws InvocationTargetException, IllegalAccessException {
    try {
      Optional<Project> projectOptional = projectRepo.findById(projectId);
      if (projectOptional.isPresent()) {
        Project project = projectOptional.get();
        Optional<Stage> stageOptional =
            project.getStageList().stream()
                .filter(stage -> stage.getStageId() == (stageId))
                .findFirst();
        if (stageOptional.isPresent()) {
          addTicket.setStage(stageOptional.get());
          logger.info("Ticket Added SuccessFully");
//          Date startingDate = addTicket.getTicketStartingDate();
//          Date endDate = addTicket.getTicketEndingDate();
//
//          System.out.println(startingDate+"--"+endDate);
          //          SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
          //          Date utilDate = dateFormat.parse(startingDate);

          Ticket ticket = ticketRepo.save(addTicket);
          triggerStart.triggerOnUpdate(null, ticket, projectId);
          Ticket ticket1 = ticketRepo.save(ticket);
          return ticket1;
        } else {
          throw new StageNotFoundException(
              "Stage with ID " + stageId + " not found for Project " + projectId);
        }
      } else {
        throw new ProjectNotFoundException("Project with ID " + projectId + " not found.");
      }
    } catch (Exception e) {
      logger.error("Error occurred while adding a new ticket: {}", e.getMessage());
      throw e;
    }
  }

  public Optional<Ticket> updateTicket(TicketDto updateTicket, Long projectId, Long stageId)
      throws InvocationTargetException, IllegalAccessException {
    try {
      Optional<Project> projectOptional = projectRepo.findById(projectId);
      if (projectOptional.isPresent()) {
        Project project = projectOptional.get();
        Optional<Stage> stageOptional =
            project.getStageList().stream()
                .filter(stage -> stage.getStageId().equals(stageId))
                .findFirst();
        logger.info("project with id {}", projectId);
        if (stageOptional.isPresent()) {
          logger.info("stage with id {}", stageId);
          Optional<Ticket> optionalTicket = ticketRepo.findById(updateTicket.getTicketId());
          if (optionalTicket.isPresent()) {
            logger.info("ticket with id {}", updateTicket.getTicketId());

            Ticket existingTicket = optionalTicket.get();
            Ticket existingTicketDummy = existingTicket.clone();
            //
            // triggerStart.updateTicketTrigger(existingTicket,updateTicket,projectId);
            existingTicket.setTicketName(updateTicket.getTicketName());
            existingTicket.setTicketStartingDate(updateTicket.getTicketStartingDate());
            existingTicket.setTicketEndingDate(updateTicket.getTicketEndingDate());
            existingTicket.setTicketAssign(updateTicket.getTicketAssign());
            existingTicket.setTicketDescription(updateTicket.getTicketDescription());
            existingTicket.setStatus(updateTicket.getStatus());
            existingTicket.setTicketPriority(updateTicket.getTicketPriority());
            Long newStageId=updateTicket.getStageId();
            if(newStageId != null){
              Stage newStage=stageRepo.findById(newStageId).get();
              existingTicket.setStage(newStage);
            }
            System.out.println("dsds" + existingTicket.getStage().getStageId());
            Ticket ticket = ticketRepo.save(existingTicket);
            triggerStart.triggerOnUpdate(existingTicketDummy, ticket, projectId);
            logger.info("Ticket Updated Successfully");
            return Optional.of(ticket);
          } else {
            throw new TicketNotFoundException(
                "Ticket with ID " + updateTicket.getTicketId() + " not found.");
          }
        } else {
          throw new StageNotFoundException(
              "Stage with ID " + stageId + " not found for Project " + projectId);
        }
      } else {
        throw new ProjectNotFoundException("Project with ID " + projectId + " not found.");
      }
    } catch (Exception e) {
      logger.error(
          "Error occurred while updating ticket with ID {}: {} :" + "{}",
          updateTicket.getTicketId(),
          e.getMessage(),
          e);
      throw e;
    }
  }

  public void deleteTicketById(long ticketId, long projectId, long stageId) {
    try {
      Optional<Project> projectOptional = projectRepo.findById(projectId);
      if (projectOptional.isPresent()) {
        System.out.println(projectOptional.isPresent());
        Project project = projectOptional.get();
        Optional<Stage> stageOptional =
            project.getStageList().stream()
                .filter(stage -> stage.getStageId() == (stageId))
                .findFirst();
        if (stageOptional.isPresent()) {
          Ticket ticket = ticketRepo.findById(ticketId).get();
          if (ticket != null) {
            //                        triggerStart.updateTicketTrigger(ticket,null,projectId);
            ticketRepo.deleteById(ticketId);
            logger.info("Ticeket Deleted SuccesFully");
          } else {
            throw new TicketNotFoundException("Ticket with ID " + ticketId + " not found.");
          }
        } else {
          throw new StageNotFoundException(
              "Stage with ID " + stageId + " not found for Project " + projectId);
        }
      } else {
        throw new ProjectNotFoundException("Project with ID " + projectId + " not found.");
      }
    } catch (Exception e) {
      logger.error("Error occurred while deleting ticket with ID {}: {}", ticketId, e.getMessage());
      throw e;
    }
  }

  public Ticket changeStage(Long ticketId, Long stageId, Long projectId) {
    try {
      Optional<Project> projectOptional = projectRepo.findById(projectId);
      if (projectOptional.isPresent()) {
        Project project = projectOptional.get();
        Optional<Stage> stageOptional =
            project.getStageList().stream()
                .filter(stage -> stage.getStageId().equals(stageId))
                .findFirst();
        if (stageOptional.isPresent()) {
          Stage newstage = stageOptional.get();
          Optional<Ticket> optionalTicket = ticketRepo.findById(ticketId);
          if (optionalTicket.isPresent()) {
            Ticket existingTicket = optionalTicket.get();
            Stage stage = existingTicket.getStage();
            existingTicket.setStage(newstage);
//            triggerStart.stageTrigger(
//                stage.getStageId(), newstage.getStageId(), existingTicket, projectId);
            ticketRepo.save(existingTicket);
            logger.info("Ticeket Updated SuccessFully");
            return existingTicket;
          } else {
            throw new TicketNotFoundException("Ticket with ID " + ticketId + " not found.");
          }
        } else {
          throw new StageNotFoundException(
              "Stage with ID " + stageId + " not found for Project " + projectId);
        }
      } else {
        throw new ProjectNotFoundException("Project with ID " + projectId + " not found.");
      }
    } catch (Exception e) {
      logger.error("Error occurred while updating ticket with ID {}: {}", ticketId, e.getMessage());
      throw e;
    }
  }
}
