package main.Service.Abstract;

import main.DTO.ProfileDTO;
import org.springframework.http.ResponseEntity;

public interface ProfileService {

    ResponseEntity getProfile(String email);

    ResponseEntity createProfile(String email, ProfileDTO userProfile);

    ResponseEntity updateProfile(String email, ProfileDTO userProfile);

}
