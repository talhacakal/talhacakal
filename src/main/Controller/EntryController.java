package main.Controller;

import main.Model.Entry;
import main.User.User;
import main.Repository.EntryRepository;
import main.User.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/entry")
public class EntryController {

    @Autowired
    private EntryRepository entryRepository;
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/{eid}")
    public Entry getEntry(@PathVariable String eid) {
        return this.entryRepository.findByEid(eid)
                .orElseThrow(() -> new HttpClientErrorException(HttpStatus.NOT_FOUND, "Not Found"));
    }

    @GetMapping("/getUserEntry/{email}")
    public List<Entry> getUserEntry(@PathVariable String email) {
        return this.entryRepository.findByUid_Email(email);
    }

    @PostMapping("/auth")
    public Object saveEntry(Authentication authentication, @RequestBody Entry entry) {
        System.out.println(authentication.getName());
        Optional<User> optionalUser = this.userRepository.findByEmail(authentication.getName());
        entry.setUid(optionalUser.get());
        return this.entryRepository.save(entry);
    }

    @PutMapping("/auth")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN') || " +
            "(@entryRepository.findByEid(#eid).isEmpty() &&" +
            "authentication.name == @entryRepository.findByEid(#entry.getEid()).get().getUid().getEmail())")
    public Object updateEntry(Authentication authentication, @RequestBody Entry entry) {
        Optional<Entry> optionalEntry = this.entryRepository.findByEid(entry.getEid());

        optionalEntry.get().setContent(entry.getContent());
        optionalEntry.get().setOpenFComment(entry.isOpenFComment());

        return this.entryRepository.save(optionalEntry.get());
    }

    @PutMapping("/auth/closeComment/{eid}")
    @PreAuthorize(" hasAnyRole('ROLE_ADMIN') || " +
            "(!@entryRepository.findByEid(#eid).isEmpty() &&" +
            "authentication.name == @entryRepository.findByEid(#eid).get().getUid().getEmail())")
    public Object closeComment(Authentication authentication, @PathVariable @P("eid") String eid) {
        Optional<Entry> optionalEntry = this.entryRepository.findByEid(eid);
        optionalEntry.get().setOpenFComment(false);
        return this.entryRepository.save(optionalEntry.get());
    }

    @PutMapping("/auth/openComment/{eid}")
    @PreAuthorize(" hasAnyRole('ROLE_ADMIN') || " +
            "(!@entryRepository.findByEid(#eid).isEmpty() &&" +
            "authentication.name == @entryRepository.findByEid(#eid).get().getUid().getEmail())")
    public Object openComment(Authentication authentication, @PathVariable @P("eid") String eid) {
        Optional<Entry> optionalEntry = this.entryRepository.findByEid(eid);
        optionalEntry.get().setOpenFComment(true);
        return this.entryRepository.save(optionalEntry.get());
    }

    @DeleteMapping("/auth/{eid}")
    @PreAuthorize(" hasAnyRole('ROLE_ADMIN') || " +
            "authentication.name == @entryRepository.findByEid(#eid).get().getUid().getEmail()")
    public Object deleteEntry(Authentication authentication, @PathVariable @P("eid") String eid) {
        Optional<User> optionalUser = this.userRepository.findByEmail(authentication.getName());
        Optional<Entry> optionalEntry = this.entryRepository.findByEid(eid);

        if (optionalEntry.isEmpty())
            return new ResponseStatusException(HttpStatus.NOT_FOUND, "Not Found");
        optionalEntry.get().setUid(null);
        this.entryRepository.delete(optionalEntry.get());
        return true;
    }


}
