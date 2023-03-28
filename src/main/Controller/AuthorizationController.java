package main.Controller;

import jakarta.transaction.Transactional;
import main.Model.Authorization;
import main.Model.Role;
import main.User.User;
import main.Repository.AuthorizationRepository;
import main.Repository.RoleRepository;
import main.User.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Transactional
@RestController
@RequestMapping("/api/authorization")
public class AuthorizationController {

    @Autowired
    private AuthorizationRepository authorizationRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;

    @GetMapping("")
    public Authorization getAuthorization(@RequestParam String email){
        return this.authorizationRepository.findByUuid_Email(email)
                .orElseThrow(() -> new RuntimeException("Not Found"));
    }

//    @PostMapping("/{uuid}")
//    public Object addAuthorization(@PathVariable String uuid){
//        Optional<User> optionalUser = this.userRepository.findByUuid(uuid);
//        Optional<Role> optionalRole = this.roleRepository.findByRole("ROLE_USER");
//        if (optionalUser.isEmpty()) return new ResponseStatusException(HttpStatus.NOT_FOUND, "User Not Found");
//
//        Authorization authorization = new Authorization(optionalUser.get(), null);
//        authorization.getRoles().add(optionalRole.get());
//        return this.authorizationRepository.save(authorization);
//    }

    @PostMapping("/addRole")
    public Object addRole(@RequestParam String email, @RequestParam String roleName){
        Optional<Authorization> optionalAuthorization = this.authorizationRepository.findByUuid_Email(email);
        Optional<Role> optionalRole = this.roleRepository.findByRole(roleName);
        if (optionalAuthorization.isEmpty() || optionalRole.isEmpty())
            return new ResponseStatusException(HttpStatus.NOT_FOUND, "Not Found");

        optionalAuthorization.get().getRoles().add(optionalRole.get());
        return optionalAuthorization.get();
    }

    @DeleteMapping("/deleteRoleUser")
    public Object deleteRoleUser(@RequestParam String email, @RequestParam String roleName){
        Optional<Authorization> optionalAuthorization = this.authorizationRepository.findByUuid_Email(email);
        Optional<Role> optionalRole = this.roleRepository.findByRole(roleName);
        if (optionalAuthorization.isEmpty() || optionalRole.isEmpty())
            return new ResponseStatusException(HttpStatus.NOT_FOUND, "Not Found");

        optionalAuthorization.get().getRoles().remove(optionalRole.get());
        return optionalAuthorization.get();
    }


}
