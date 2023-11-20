package com.workflow.controller;

import com.workflow.customException.ProjectNotFoundException;
import com.workflow.customException.StageNotFoundException;
import com.workflow.customException.TicketNotFoundException;
import com.workflow.customException.UserNotFoundException;
import com.workflow.dto.TicketDto;
import com.workflow.entity.CustomResponseEntity;
import com.workflow.entity.Ticket;
import com.workflow.service.TicketService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("projects")
public class TicketController {

    private static final Logger logger = LoggerFactory.getLogger(TicketController.class);

    @Autowired
    private TicketService ticketService;

    /**
     * Retrieve a list of tickets for a specific project and stage.
     *
     * @param projectId The ID of the project.
     * @param stageId   The ID of the stage.
     * @return A ResponseEntity containing the list of tickets or an error message.
     */
    @GetMapping("/{projectId}/stages/{stageId}/tickets")
    public ResponseEntity<?> getTicketsByProjectAndStage(@PathVariable long projectId, @PathVariable long stageId) {
        try {
            List<Ticket> tickets = ticketService.getTicketsByProjectAndStage(projectId, stageId);
            logger.info("Retrieved List of Tickets in a Stage");
            return ResponseEntity.ok(
                    new CustomResponseEntity("List of Tickets in Stage " + stageId, 200, tickets)
            );
        } catch (ProjectNotFoundException e) {
            logger.error("Project with ID {} not found: {}", projectId, e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new CustomResponseEntity("Project with ID " + projectId + " not found", 404, null)
            );
        } catch (StageNotFoundException e) {
            logger.error("Stage with ID {} not found: {}", stageId, e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new CustomResponseEntity("Stage with ID " + stageId + " not found", 404, null)
            );
        } catch (Exception e) {
            logger.error("Error occurred while fetching tickets for Project ID {} and Stage ID {}: {}", projectId, stageId, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new CustomResponseEntity("Internal Server Error", 500, null)
            );
        }
    }

    /**
     * Retrieve a ticket by its ID within a specific project and stage.
     *
     * @param ticketId  The ID of the ticket.
     * @param projectId The ID of the project.
     * @param stageId   The ID of the stage.
     * @return A ResponseEntity containing the ticket or an error message.
     */
    @GetMapping("/{projectId}/stages/{stageId}/tickets/{ticketId}")
    public ResponseEntity<?> getTicketById(
            @PathVariable long projectId,
            @PathVariable long stageId,
            @PathVariable long ticketId) {
        try {
            Ticket ticket = ticketService.getTicketById(ticketId, projectId, stageId).get();

            TicketDto ticketDto = new TicketDto();
            ticketDto.setTicketId(ticket.getTicketId());
            ticketDto.setTicketName(ticket.getTicketName());
            ticketDto.setTicketAssign(ticket.getTicketAssign());
            ticketDto.setTicketStartingDate(ticket.getTicketStartingDate());
            ticketDto.setTicketEndingDate(ticket.getTicketEndingDate());
            ticketDto.setTicketDescription(ticket.getTicketDescription());
            ticketDto.setStageId(ticket.getStage().getStageId());
            ticketDto.setStatus(ticket.getStatus());
            ticketDto.setTicketPriority(ticket.getTicketPriority());

            logger.info("Retrieved Ticket by Ticket ID");
            return ResponseEntity.ok(
                    new CustomResponseEntity("Retrieved Ticket by Ticket ID", 200, ticketDto)
            );
        } catch (TicketNotFoundException e) {
            logger.error("Ticket with ID {} not found: {}", ticketId, e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new CustomResponseEntity("Ticket with ID " + ticketId + " not found", 404, null)
            );
        } catch (ProjectNotFoundException e) {
            logger.error("Project with ID {} not found: {}", projectId, e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new CustomResponseEntity("Project with ID " + projectId + " not found", 404, null)
            );
        } catch (StageNotFoundException e) {
            logger.error("Stage with ID {} not found: {}", stageId, e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new CustomResponseEntity("Stage with ID " + stageId + " not found", 404, null)
            );
        } catch (Exception e) {
            logger.error("Error occurred while retrieving a ticket by ID {}: {}", ticketId, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new CustomResponseEntity("Internal Server Error", 500, null)
            );
        }
    }

    /**
     * Add a new ticket to a specific project, stage, and user.
     *
     * @param addTicket The ticket to be added.
     * @param projectId The ID of the project.
     * @param stageId   The ID of the stage.
     * @return A ResponseEntity containing the added ticket or an error message.
     */
    @PostMapping("/{projectId}/stages/{stageId}/tickets")
    public ResponseEntity<?> addTicket(
            @RequestBody Ticket addTicket,
            @PathVariable long projectId,
            @PathVariable long stageId) {
        try {
            Ticket savedTicket = ticketService.addTicket(addTicket, projectId, stageId);
            TicketDto ticketDto = new TicketDto();
            ticketDto.setTicketId(savedTicket.getTicketId());
            ticketDto.setTicketName(savedTicket.getTicketName());
            ticketDto.setTicketAssign(savedTicket.getTicketAssign());
            ticketDto.setTicketStartingDate(savedTicket.getTicketStartingDate());
            ticketDto.setTicketEndingDate(savedTicket.getTicketEndingDate());
            ticketDto.setTicketDescription(savedTicket.getTicketDescription());
//            System.out.println(updatedTicket.get().getStage().getStageId());
            if(savedTicket.getStage() != null)
                ticketDto.setStageId(savedTicket.getStage().getStageId());
            ticketDto.setStatus(savedTicket.getStatus());
            ticketDto.setTicketPriority(savedTicket.getTicketPriority());
            logger.info("Added Ticket Successfully");
            return ResponseEntity.status(HttpStatus.CREATED).body(
                    new CustomResponseEntity("Ticket added successfully", 201, ticketDto)
            );
        }catch (ProjectNotFoundException e) {
            logger.error("Project with ID {} not found: {}", projectId, e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new CustomResponseEntity("Project with ID " + projectId + " not found", 404, null)
            );
        } catch (StageNotFoundException e) {
            logger.error("Stage with ID {} not found: {}", stageId, e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new CustomResponseEntity("Stage with ID " + stageId + " not found", 404, null)
            );
        } catch (Exception e) {
            logger.error("Error occurred while adding a new ticket: {} ,{} ", e.getMessage(),e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new CustomResponseEntity("User ID Already Assigne Or May Be You Entered Empty Ticket Name, Please Provide Valid Input.", 500, null)
            );
        }
    }

    /**
     * Update an existing ticket within a specific project and stage.
     *
     * @param updateTicket The updated ticket data.
     * @param projectId    The ID of the project.
     * @param stageId      The ID of the stage.
     * @return A ResponseEntity containing the updated ticket or an error message.
     */
    @PutMapping("/{projectId}/stages/{stageId}/tickets")
    public ResponseEntity<?> updateTicket(
            @RequestBody TicketDto ticketRequest,
            @PathVariable long projectId,
            @PathVariable long stageId) {
        try {

            Optional<Ticket> updatedTicket = ticketService.updateTicket(ticketRequest, projectId, stageId);
            logger.info("Updated Ticket Successfully");

            TicketDto ticketDto = new TicketDto();
            ticketDto.setTicketId(updatedTicket.get().getTicketId());
            ticketDto.setTicketName(updatedTicket.get().getTicketName());
            ticketDto.setTicketAssign(updatedTicket.get().getTicketAssign());
            ticketDto.setTicketStartingDate(updatedTicket.get().getTicketStartingDate());
            ticketDto.setTicketEndingDate(updatedTicket.get().getTicketEndingDate());
            ticketDto.setTicketDescription(updatedTicket.get().getTicketDescription());
//            System.out.println(updatedTicket.get().getStage().getStageId());
            if(updatedTicket.get().getStage() != null)
                ticketDto.setStageId(updatedTicket.get().getStage().getStageId());
            ticketDto.setStatus(updatedTicket.get().getStatus());
            ticketDto.setTicketPriority(updatedTicket.get().getTicketPriority());
            return ResponseEntity.status(HttpStatus.CREATED).body(
                    new CustomResponseEntity("Ticket updated successfully", 200, ticketDto)
            );
        } catch (TicketNotFoundException e) {
            logger.error("Ticket with ID {} not found: {}", ticketRequest.getTicketId(), e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new CustomResponseEntity("Ticket with ID " + ticketRequest.getTicketId() + " not found", 404, null)
            );
        } catch (ProjectNotFoundException e) {
            logger.error("Project with ID {} not found: {}", projectId, e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new CustomResponseEntity("Project with ID " + projectId + " not found", 404, null)
            );
        } catch (StageNotFoundException e) {
            logger.error("Stage with ID {} not found: {}", stageId, e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new CustomResponseEntity("Stage with ID " + stageId + " not found", 404, null)
            );
        } catch (Exception e) {
            logger.error("Error occurred while updating ticket with ID {}: {} {}", ticketRequest.getTicketId(), e.getMessage(),e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new CustomResponseEntity("Internal Server Error", 500, null)
            );
        }
    }


    /**
     * Delete a ticket by its ID within a specific project and stage.
     *
     * @param ticketId  The ID of the ticket to be deleted.
     * @param projectId The ID of the project.
     * @param stageId   The ID of the stage.
     * @return A ResponseEntity with no content or an error message.
     */
    @DeleteMapping("/{projectId}/stages/{stageId}/tickets/{ticketId}")
    public ResponseEntity<?> deleteTicketById(@PathVariable long ticketId,
            @PathVariable long projectId,
            @PathVariable long stageId) {
        try {
            ticketService.deleteTicketById(ticketId, projectId, stageId);
            logger.info("Deleted Data Successfully");
            return ResponseEntity.noContent().build();
        } catch (TicketNotFoundException e) {
            logger.error("Ticket with ID {} not found: {}", ticketId, e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new CustomResponseEntity("Ticket with ID " + ticketId + " not found", 404, null)
            );
        } catch (ProjectNotFoundException e) {
            logger.error("Project with ID {} not found: {}", projectId, e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new CustomResponseEntity("Project with ID " + projectId + " not found", 404, null)
            );
        } catch (StageNotFoundException e) {
            logger.error("Stage with ID {} not found: {}", stageId, e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new CustomResponseEntity("Stage with ID " + stageId + " not found", 404, null)
            );
        } catch (Exception e) {
            logger.error("Error occurred while deleting ticket with ID {}: {}", ticketId, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new CustomResponseEntity("Internal Server Error", 500, null)
            );
        }
    }

    @PutMapping("/{projectId}/changestages/{stageId}/tickets/{ticketId}")
    public ResponseEntity<?> changeStage(@PathVariable Long ticketId,@PathVariable Long stageId,@PathVariable Long projectId){
        try{
            Ticket savedticket=ticketService.changeStage(ticketId,stageId,projectId);
            return ResponseEntity.status(HttpStatus.CREATED).body(
                    new CustomResponseEntity<>("Stage of ticket changed",200,savedticket)
            );
        }catch (TicketNotFoundException e) {
            logger.error("Ticket with ID {} not found: {}", ticketId, e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new CustomResponseEntity("Ticket with ID " + ticketId + " not found", 404, null)
            );
        }catch (ProjectNotFoundException e) {
            logger.error("Project with ID {} not found: {}", projectId, e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new CustomResponseEntity("Project with ID " + projectId + " not found", 404, null)
            );
        }catch (StageNotFoundException e) {
            logger.error("Stage with ID {} not found: {}", stageId, e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new CustomResponseEntity("Stage with ID " + stageId + " not found", 404, null)
            );
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new CustomResponseEntity("Internal Server Error", 500, null)
            );
        }
    }
}