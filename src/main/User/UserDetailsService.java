package main.User;

import main.Model.Authentication;
import main.Model.Authorization;
import main.Repository.AuthenticationRepository;
import main.Repository.AuthorizationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

@Service
public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AuthorizationRepository authorizationRepository;
    @Autowired
    private AuthenticationRepository authenticationRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        Optional<User> user = this.userRepository.findByEmail(username);
        if (user.isEmpty()) {
            throw new UsernameNotFoundException("User details not found for the user : " + username);
        } else {
            Optional<Authorization> authorization = this.authorizationRepository.findByUuid_Uuid(user.get().getUuid());
            Optional<Authentication> authentication = this.authenticationRepository.findByUuid_Uuid(user.get().getUuid());
            authorization.get().getRoles().forEach( role -> {
                authorities.add(new SimpleGrantedAuthority(role.getRole()));
            });
            return new org.springframework.security.core.userdetails.User(user.get().getEmail(), authentication.get().getPassword(), authorities);
        }
    }
}


















