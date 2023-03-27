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

    @GetMapping("/all")
    public List<Authorization> getAll(){
        return this.authorizationRepository.findAll();
    }

    @GetMapping("")
    public Authorization getAuthorization(@RequestParam String uuid){
        return this.authorizationRepository.findByUuid_Uuid(uuid)
                .orElseThrow(() -> new RuntimeException("Not Found"));
    }

    @PostMapping("/{uuid}")
    public Object addAuthorization(@PathVariable String uuid){
        Optional<User> optionalUser = this.userRepository.findByUuid(uuid);
        Optional<Role> optionalRole = this.roleRepository.findByRole("ROLE_USER");
        if (optionalUser.isEmpty()) return new ResponseStatusException(HttpStatus.NOT_FOUND, "User Not Found");

        Authorization authorization = new Authorization(optionalUser.get(), null);
        authorization.getRoles().add(optionalRole.get());
        return this.authorizationRepository.save(authorization);
    }

    @PostMapping("/addRole/{uuid}")
    public Object addRole(@PathVariable String uuid, @RequestParam String roleName){
        Optional<User> optionalUser = this.userRepository.findByUuid(uuid);
        Optional<Role> optionalRole = this.roleRepository.findByRole(roleName);
        if (optionalUser.isEmpty() || optionalRole.isEmpty())
            return new ResponseStatusException(HttpStatus.NOT_FOUND, "Not Found");

        Optional<Authorization> optionalAuthorization = this.authorizationRepository.findByUuid_Uuid(uuid);
        optionalAuthorization.get().getRoles().add(optionalRole.get());
        return optionalAuthorization.get();
    }


}
