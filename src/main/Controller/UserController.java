package main.Controller;

import main.User.User;
import main.User.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/all")
    public List<User> getAllUser() {
        return this.userRepository.findAll();
    }
    @GetMapping("")
    public User getUser(@RequestParam String uuid) {
        return this.userRepository.findByUuid(uuid)
                .orElseThrow(() -> new RuntimeException("User Not Found"));
    }

    @PostMapping("")
    public User createUser(@RequestBody User user) {
        return this.userRepository.save(user);
    }

    @PutMapping("")
    public Object updateUser(@RequestBody User user) {
        Optional<User> userOptional = this.userRepository.findByUuid(user.getUuid());

        if (userOptional.isEmpty()) return new ResponseStatusException(HttpStatus.NOT_FOUND, "User Not Found");
        userOptional.get().setEmail(user.getEmail());
        return this.userRepository.save(userOptional.get());
    }


}
