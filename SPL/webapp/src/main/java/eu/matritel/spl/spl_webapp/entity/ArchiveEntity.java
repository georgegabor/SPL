package eu.matritel.spl.spl_webapp.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "archivelist")
public class ArchiveEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "archiveId")
    @Size(max = 64)
    private String archiveId;

    @Column(name = "url")
    @Size(max = 512)
    private String url;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "checklist_id")
    private CheckList checkListItem;

    @ManyToOne(fetch = FetchType.LAZY)
    private CustomSessionEntity sessionId;



}