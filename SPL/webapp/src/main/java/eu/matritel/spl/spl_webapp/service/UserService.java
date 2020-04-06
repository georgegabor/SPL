package eu.matritel.spl.spl_webapp.service;

import eu.matritel.spl.spl_webapp.entity.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;
import java.util.Optional;

public interface UserService extends UserDetailsService {
    User create(User user);

    List<User> getAllUsers();
    List<User> getAllEngineers();
    List<User> getAllAuditors();

    Optional<User> findById(int id);
    Optional<User> findByEmail(String email);

    public UserDetails loadUserByUsername(String username);
    public boolean getUserByStatus(User user);

}