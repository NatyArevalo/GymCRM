package com.gymcrm.gymcrm.Entities;

import java.time.LocalDate;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
@Table(name = "Training")
public class Training {
    @Id
    @Column(name = "training_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "trainee_id", referencedColumnName = "trainee_id", nullable = false)
    private Trainee trainee;

    @ManyToOne
    @JoinColumn(name = "trainer_id", referencedColumnName = "trainer_id", nullable = false)
    private Trainer trainer;

    @NotNull
    private String trainingName;

    
    @ManyToOne
    @JoinColumn(name = "trainingType_id", referencedColumnName = "id", nullable = false)
    private TrainingType trainingType;

    @Column(name = "traning_date")
    private LocalDate trainingDate;

    @NotNull
    private Double duration;

    @Override
    public String toString() {
        return "Training{" +
                "id='" + id + '\'' +
                ", trainee=" + trainee +
                ", trainer=" + trainer +
                ", trainingName='" + trainingName + '\'' +
                ", trainingType=" + trainingType +
                ", trainingDate=" + trainingDate +
                ", duration=" + duration +
                '}';
    }
}
