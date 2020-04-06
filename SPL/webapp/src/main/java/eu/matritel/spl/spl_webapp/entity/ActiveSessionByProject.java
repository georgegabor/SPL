package eu.matritel.spl.spl_webapp.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "activeSessionByProject")

public class ActiveSessionByProject  {

    @Id
    @Column(name = "projectId",unique = true)
    private int projectId;
    @Column(unique = true,name = "sessionId")
    private String sessionId;
    @Column
    private int archiveCount;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "sessionActivated")
    private Date sessionActivated;
}