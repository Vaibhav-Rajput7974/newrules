package com.workflow.dto;

import com.fasterxml.jackson.databind.ser.Serializers;
import com.workflow.entity.User;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Data
public class TicketDto {

        private long ticketId;

        private String ticketName;

        private String ticketDescription;

        private User ticketAssign;

        private Date ticketStartingDate;

        private Date ticketEndingDate;

        private Long stageId;

        private String status;

        private String ticketPriority;
}
