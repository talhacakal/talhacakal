package main.Controller;

import main.Model.Profile;
import main.User.User;
import main.Repository.ProfileRepository;
import main.User.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
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

    @GetMapping("/{email}")
    public Profile getProfile(@PathVariable String email) {
        return this.profileRepository.findByUuid_Email(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User Not Found"));
    }

    @PostMapping("")
    public Object createProfile(Authentication authentication, @RequestBody Profile user) {
        Optional<User> optionalUser = this.userRepository.findByEmail(authentication.getName());
        if (!this.profileRepository.findByUuid_Email(authentication.getName()).isEmpty()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Already exist!");
        }
        user.setUuid(optionalUser.get());
        return this.profileRepository.save(user);
    }

    @PutMapping("")
    public Object updateProfile(Authentication authentication, @RequestBody Profile user) {
        Optional<Profile> optionalProfile = this.profileRepository.findByUuid_Email(authentication.getName());

        if (optionalProfile.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Create profile!");
        }

        optionalProfile.get().setName(user.getName());
        optionalProfile.get().setLastName(user.getLastName());
        optionalProfile.get().setBirth(user.getBirth());
        optionalProfile.get().setPersonalInfo(user.getPersonalInfo());
        optionalProfile.get().setLocation(user.getLocation());

        return this.profileRepository.save(optionalProfile.get());
    }


}
