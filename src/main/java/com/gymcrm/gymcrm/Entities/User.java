package com.gymcrm.gymcrm.Entities;

import com.gymcrm.gymcrm.Enumerators.Rol;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;


@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Users")
public class User {
    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotNull
    private String firstName;

    @NotNull
    private String lastName;

    @NotNull
    private String username;

    @NotNull
    private String password;

    @NotNull
    private Boolean isActive;

    @Enumerated(EnumType.STRING)
    private Rol rol;

    @OneToMany(mappedBy = "user")
    private List<Token> tokens;

}
