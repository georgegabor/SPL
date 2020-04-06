package eu.matritel.spl.spl_webapp.entity;

import lombok.Getter;

@Getter
public enum Role {
    ADMIN("ADMIN"),
    AUDITOR("AUDITOR"),
    ENGINEER("ENGINEER");

    private String role;

    Role(String role) {
        this.role = role;
    }
}
