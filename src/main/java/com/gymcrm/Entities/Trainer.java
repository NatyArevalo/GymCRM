package com.gymcrm.Entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Table(name = "Trainer")
public class Trainer{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "trainer_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "user_id", nullable = false)
    private User user;
    @ManyToOne
    @JoinColumn(name = "specialization_id", referencedColumnName = "id", nullable = false)
    private TrainingType specialization;
    
    public void setSpecialization(TrainingType specialization) {
        this.specialization = specialization;
    }
    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "Trainer{" + "id=" + user.getId() +
                ", firstName='" + user.getFirstName() + '\'' +
                ", lastName='" + user.getLastName() + '\'' +
                ", username='" + user.getUsername() + '\'' +
                ", password='" + user.getPassword() + '\'' +
                ", isActive=" + user.getIsActive() + '\'' +
                ", specialization=" + specialization +
                '}';
    }
}
