package main.Controller;

import main.Model.Role;
import main.Repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/role")
public class RoleController {

    @Autowired
    private RoleRepository roleRepository;

    @GetMapping("/all")
    public List<Role> getAll() {
        return this.roleRepository.findAll();
    }
//
//    @GetMapping("")
//    public Role getRole(@RequestParam String roleName) {
//        return this.roleRepository.findByRole(roleName)
//                .orElseThrow(() -> new RuntimeException("Role Not Found"));
//    }

    @PostMapping("")
    public Role saveRole(@RequestParam String roleName) {
        return this.roleRepository.save(new Role(roleName));
    }

    @PutMapping("")
    public Object updateRole(@RequestBody Role user) {
        Optional<Role> optionalRole = this.roleRepository.findById(user.getId());
        if (optionalRole.isEmpty()) return new ResponseStatusException(HttpStatus.NOT_FOUND, "Role Not Found");
        optionalRole.get().setRole(user.getRole());
        return this.roleRepository.save(optionalRole.get());
    }


}
