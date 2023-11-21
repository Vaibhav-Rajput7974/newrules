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
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Ticket implements Cloneable {

    @Override
    public Ticket clone() {
        try {
            Ticket clonedTicket = (Ticket) super.clone();


            return clonedTicket;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError(e);
        }
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long ticketId;

    @NotBlank(message = "Ticket Name Cannot Be Blank")
    private String ticketName;

    private String ticketDescription;

    @JoinColumn(name = "user_Id")
    @ManyToOne
    private User ticketAssign;

    private Date ticketStartingDate;

    private Date ticketEndingDate;

    private String ticketPriority;

    @JsonBackReference
    @ManyToOne
    private Stage stage;

    private String status;

    private Long persentage;

    @Override
    public String toString() {
        return "Ticket{" +
                "ticketId=" + ticketId +
                ", ticketName='" + ticketName + '\'' +
                ", ticketDescription='" + ticketDescription + '\'' +
                ", ticketAssign='" + ticketAssign + '\'' +
                ", ticketPriority='" + ticketPriority + '\'' +
                ", stage=" + stage +
                '}';
    }
}









