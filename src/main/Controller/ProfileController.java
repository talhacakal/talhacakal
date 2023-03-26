package main.Controller;

import main.Model.Profile;
import main.Model.User;
import main.Repository.ProfileRepository;
import main.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/profile")
public class ProfileController {

    @Autowired
    private ProfileRepository profileRepository;
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/all")
    public List<Profile> getAll() {
        return this.profileRepository.findAll();
    }

    @GetMapping("/{uuid}")
    public Profile getProfile(@PathVariable String uuid) {
        return this.profileRepository.findByUuid_Uuid(uuid)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User Not Found"));
    }

    @PostMapping("/{uuid}")
    public Object createProfile(@PathVariable String uuid, @RequestBody Profile user) {
        Optional<User> optionalUser = this.userRepository.findByUuid(uuid);
        if (optionalUser.isEmpty()) return new ResponseStatusException(HttpStatus.NOT_FOUND, "User Not Found");

        user.setUuid(optionalUser.get());
        return this.profileRepository.save(user);
    }

    @PutMapping("/{uuid}")
    public Object updateProfile(@RequestBody Profile user) {
        Optional<User> optionalUser = this.userRepository.findByUuid(user.getUuid().getUuid());
        Optional<Profile> optionalProfile = this.profileRepository.findById(user.getId());

        if (optionalUser.isEmpty() || optionalProfile.isEmpty())
            return new ResponseStatusException(HttpStatus.NOT_FOUND, "User Not Found");

        optionalProfile.get().setName(user.getName());
        optionalProfile.get().setLastName(user.getLastName());
        optionalProfile.get().setBirth(user.getBirth());
        optionalProfile.get().setPersonalInfo(user.getPersonalInfo());
        optionalProfile.get().setLocation(user.getLocation());

        return this.profileRepository.save(optionalProfile.get());
    }


}
