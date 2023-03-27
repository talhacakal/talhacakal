package main.Controller;

import main.Model.Authentication;
import main.User.User;
import main.Repository.AuthenticationRepository;
import main.User.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/authentication")
public class AuthenticationController {

    @Autowired
    private AuthenticationRepository authenticationRepository;
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/getAll")
    private List<Authentication> getAll() {
        return this.authenticationRepository.findAll();
    }

    @GetMapping("")
    private Authentication getAuthentication(@RequestParam String uuid) {
        return this.authenticationRepository.findByUuid_Uuid(uuid)
                .orElseThrow(() -> new RuntimeException("User Not Found"));
    }

    @PostMapping("/{uuid}")
    private Object saveAuthentication(@PathVariable String uuid, @RequestBody Authentication user) {
        Optional<User> userOptional = this.userRepository.findByUuid(uuid);
        if (userOptional.isEmpty()) return new ResponseStatusException(HttpStatus.NOT_FOUND, "User Not Found");

        Authentication authentication = Authentication.builder()
                .password(user.getPassword()).uuid(userOptional.get()).build();
        return this.authenticationRepository.save(authentication);
    }

    @PutMapping("/{uuid}")
    private Object updateAuthentication(@PathVariable String uuid, @RequestBody Authentication user) {
        Optional<User> userOptional = this.userRepository.findByUuid(uuid);
        Optional<Authentication> authenticationOptional = this.authenticationRepository.findById(user.getId());
        if (userOptional.isEmpty() || authenticationOptional.isEmpty())
            return new ResponseStatusException(HttpStatus.NOT_FOUND, "User Not Found");

        authenticationOptional.get().setPassword(user.getPassword());
        return this.authenticationRepository.save(authenticationOptional.get());
    }


}
