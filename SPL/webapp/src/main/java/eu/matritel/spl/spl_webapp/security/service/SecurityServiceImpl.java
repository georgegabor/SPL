package eu.matritel.spl.spl_webapp.security.service;

import eu.matritel.spl.spl_webapp.entity.User;
import eu.matritel.spl.spl_webapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.Optional;

@Service
public class SecurityServiceImpl implements SecurityService {
    private static BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    @Autowired
    private UserService userService;

    @Override
    public String getEncodedText(String text) {
        return encoder.encode(text);
    }

    @Override
    public Optional<User> findLoggedInUser() {
        Object userDetails = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (userDetails instanceof UserDetails) {
            Optional<User> user = userService.findByEmail(((UserDetails)userDetails).getUsername());
            return user;
        }
        return Optional.empty();
    }
}