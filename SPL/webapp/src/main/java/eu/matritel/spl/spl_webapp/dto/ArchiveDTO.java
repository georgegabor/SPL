package eu.matritel.spl.spl_webapp.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@Builder

public class ArchiveDTO {
    private String project;
    private String checkList;
    private String session;
    private String archivieId;
    private String url;

    public ArchiveDTO(String project, String checkList, String session, String archivieId, String url) {
        this.project = project;
        this.checkList = checkList;
        this.session = session;
        this.archivieId = archivieId;
        this.url = url;
    }
}
