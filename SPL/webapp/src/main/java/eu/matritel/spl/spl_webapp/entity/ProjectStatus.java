package eu.matritel.spl.spl_webapp.entity;

public enum ProjectStatus {
    DRAFT("DRAFT"),
    STARTED("STARTED"),
    FINISHED("FINISHED"),
    ARCHIVED("ARCHIVED"),
    DELETED("DELETED");

    private String projectStatus;

    ProjectStatus(String projectStatus) {
        this.projectStatus = projectStatus;
    }
}
