package main.Security.Controller;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import main.Model.Authentication;
import main.Model.Authorization;
import main.Repository.AuthenticationRepository;
import main.Repository.AuthorizationRepository;
import main.Repository.RoleRepository;
import main.User.User;
import main.User.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Transactional
public class LoginController {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final AuthenticationRepository authenticationRepository;
    private final AuthorizationRepository authorizationRepository;
    private final RoleRepository roleRepository;

    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestParam String username, @RequestParam String password){
        User user = new User();
        Authentication authentication = new Authentication();
        Authorization authorization = new Authorization();

        String hashPassword = passwordEncoder.encode(password);
        authentication.setPassword(hashPassword);
        user.setEmail(username);
        user.setProperties("yok bişi kanks sg");
        authorization.getRoles().add(this.roleRepository.findByRole("ROLE_USER").get());

        User createdUser = this.userRepository.save(user);
        authentication.setUuid(createdUser);
        authorization.setUuid(createdUser);
        this.authenticationRepository.save(authentication);
        this.authorizationRepository.save(authorization);

        return ResponseEntity.ok(createdUser);
    }

}
