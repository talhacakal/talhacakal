package main.Controller;

import main.Model.Entry;
import main.Model.User;
import main.Repository.EntryRepository;
import main.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

    @GetMapping("/getById/{uuid}")
    public List<Entry> getById(@PathVariable String uuid) {
        return this.entryRepository.findByUid_Uuid(uuid);
    }

    @PostMapping("/{uuid}")
    public Object saveEntry(@PathVariable String uuid, @RequestBody Entry entry) {
        Optional<User> optionalUser = this.userRepository.findByUuid(uuid);
        if (optionalUser.isEmpty()) return new ResponseStatusException(HttpStatus.NOT_FOUND, "Not Found");
        entry.setUid(optionalUser.get());
        return this.entryRepository.save(entry);
    }

    @PutMapping("/{uuid}")
    public Object updateEntry(@PathVariable String uuid, @RequestBody Entry entry) {
        Optional<User> optionalUser = this.userRepository.findByUuid(uuid);
        Optional<Entry> optionalEntry = this.entryRepository.findByEid(entry.getEid());
        if (optionalUser.isEmpty() || optionalEntry.isEmpty())
            return new ResponseStatusException(HttpStatus.NOT_FOUND, "Not Found");

        optionalEntry.get().setContent(entry.getContent());
        optionalEntry.get().setOpenFComment(entry.isOpenFComment());

        return this.entryRepository.save(entry);
    }


}
