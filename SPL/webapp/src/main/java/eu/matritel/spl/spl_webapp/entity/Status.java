package eu.matritel.spl.spl_webapp.entity;

public enum Status {
    ACTIVE("ACTIVE"),
    INACTIVE("INACTIVE");

    private String status;

    Status(String status) {
        this.status = status;
    }
}
