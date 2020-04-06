package eu.matritel.spl.spl_webapp.service;

import eu.matritel.spl.spl_webapp.entity.Status;
import eu.matritel.spl.spl_webapp.entity.User;
import eu.matritel.spl.spl_webapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    public User create(User user) {
        return userRepository.save(user);
    }

    public List<User> getAllUsers() { return userRepository.findAll(); }

    @Override
    public List<User> getAllAuditors() {
        return userRepository.getAllAuditors();
    }

    @Override
    public List<User> getAllEngineers() {
        return userRepository.getAllEngineers();
    }

    @Override
    public Optional<User> findById(int id) {
        return userRepository.findById(id);
    }

    @Override
    public Optional<User> findByEmail(String email) {return userRepository.findByEmail(email);}

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByEmail(username);
        if(user.isPresent()) {
            Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
            grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_" + user.get().getRole().toString()));

            return new org.springframework.security.core.userdetails.User(user.get().getEmail(), user.get().getPassword(), grantedAuthorities);
        }
        return null;
    }

    @Override
    public boolean getUserByStatus(User user) {
        return user.getStatus() == Status.ACTIVE;
    }
}