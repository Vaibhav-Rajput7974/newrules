package com.workflow.dto;

import com.workflow.entity.User;
import lombok.Data;

import java.time.LocalDate;
import java.util.Date;

@Data
public class TicketDto {

        private long ticketId;

        private String ticketName;

        private String ticketDescription;

        private String ticketAssign;

        private Date ticketStartingDate;

        private Date ticketEndingDate;

        private Long stageId;

        private String status;

        private String ticketPriority;
}
