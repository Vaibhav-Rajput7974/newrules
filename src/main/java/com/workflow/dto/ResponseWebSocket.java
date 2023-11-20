package com.workflow.dto;

import com.workflow.entity.Ticket;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;


@Service
@Data
public class ResponseWebSocket {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    public void sendResponse(Ticket ticket){
        TicketDto ticketDto = new TicketDto();
        ticketDto.setTicketId(ticket.getTicketId());
        ticketDto.setTicketName(ticket.getTicketName());
        ticketDto.setTicketAssign(ticket.getTicketAssign());
        ticketDto.setTicketStartingDate(ticket.getTicketStartingDate());
        ticketDto.setTicketEndingDate(ticket.getTicketEndingDate());
        ticketDto.setTicketDescription(ticket.getTicketDescription());
        if(ticket.getStage() != null)
            ticketDto.setStageId(ticket.getStage().getStageId());
        ticketDto.setStatus(ticket.getStatus());
        ticketDto.setTicketPriority(ticket.getTicketPriority());
        messagingTemplate.convertAndSend("/topic/ticket-updates",  ticketDto);
    }
}
