package eu.matritel.spl.spl_webapp.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "checklist")
public class CheckList {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @CreationTimestamp
    @Column(name = "created_at")
    //Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @Column(name = "updated_at")
    @UpdateTimestamp
    private Date updatedAt;

    @Column(name = "severity")
    private int severity;

    @Column(name = "description")
    private String description;

    @Column(name = "header", columnDefinition = "TINYINT(1)")
    private Boolean header = Boolean.FALSE;

    @ManyToOne
    @JoinColumn(name = "parent_id")
    private CheckList parent;

    @Column(name = "name")
    @Size(max = 32)
    private String name;

    @Column(name = "status")
    private String status;

    @Column(name = "ord")
    private Integer order;

    @Column(name = "itemNumber")
    private String itemNumber;

    @Column(name = "comment")
    private String comment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    private Project project;

    @OneToMany(mappedBy = "checkListItem", cascade = CascadeType.ALL)
    private Set<CheckListSnapshots> snapshots = new HashSet<>();

    @OneToMany(mappedBy = "checkListItem", cascade = CascadeType.ALL)
    private Set<ArchiveEntity> archiveEntities = new HashSet<>();
}
