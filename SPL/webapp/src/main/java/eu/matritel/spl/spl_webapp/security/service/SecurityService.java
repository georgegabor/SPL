package eu.matritel.spl.spl_webapp.security.service;

import eu.matritel.spl.spl_webapp.entity.User;

import java.util.Optional;

public interface SecurityService {
    String getEncodedText(String text);
    Optional<User> findLoggedInUser();
}