package eu.matritel.spl.spl_webapp.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "project")
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "name")
    @Size(max = 256)
    private String name;

    @Column(name = "comment")
    private String comment;

    @Column(name = "address")
    @Size(max = 512)
    private String address;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="auditor_id")
    private User auditor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="engineer_id")
    private User engineer;

    @DateTimeFormat(pattern = "yyyy-mm-dd")
    @Column(name = "date")
    private Date date;

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL)
    private Set<CheckList> checkListSet;

    @Column(name = "template")
    private Boolean template = false;

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL)
    private Set<CustomSessionEntity> customSessionEntityList;

    @Column(name = "project_status")
    private ProjectStatus projectStatus = ProjectStatus.DRAFT;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "site_id")
    private Site site;
}
