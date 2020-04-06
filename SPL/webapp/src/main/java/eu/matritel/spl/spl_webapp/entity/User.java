package eu.matritel.spl.spl_webapp.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "name")
    @Size(max = 512)
    private String name;

    @Column(name = "password")
    private String password;

    @Column(name = "status")
    @Enumerated (EnumType.STRING)
    private Status status = Status.ACTIVE;

    @Column(name = "email")
    @Size(max = 512)
    private String email;

    @Column(name = "role")
    @Enumerated (EnumType.STRING)
    private Role role;

    @OneToMany(mappedBy = "auditor", cascade = CascadeType.ALL)
    private Set<Project> auditorProjects;

    @OneToMany(mappedBy = "engineer", cascade = CascadeType.ALL)
    private Set<Project> engineerProjects;
}