package com.workflow.entity;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long ticketId;

    @NotBlank(message = "Ticket Name Cannot Be Blank")
    private String ticketName;

    private String ticketDescription;

    private String ticketAssign;

    private Date ticketStartingDate;

    private Date ticketEndingDate;

    private String ticketPriority;

    @JsonBackReference
    @ManyToOne
    private Stage stage;

    private String status;
}









