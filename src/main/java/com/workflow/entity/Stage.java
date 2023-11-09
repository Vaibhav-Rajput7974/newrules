package com.workflow.entity;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Stage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long stageId;
    @NotBlank(message = "Stage Name Cannot Be Blank")
    private String stageName;
    private String stageDescription;

    @JsonBackReference
    @ManyToOne
    private Project project;

    @JsonManagedReference
    @OneToMany(mappedBy = "stage" , cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    private List<Ticket> ticketList;
    public Stage(String stageName) {
        this.stageName = stageName;
    }

    @Override
    public String toString() {
        return "Stage{" +
                "stageId=" + stageId +
                ", stageName='" + stageName + '\'' +
                ", stageDescription='" + stageDescription + '\'' +
                '}';
    }
}