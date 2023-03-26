package main.Controller;

import main.Model.Comment;
import main.Model.Entry;
import main.Model.User;
import main.Repository.CommentRepository;
import main.Repository.EntryRepository;
import main.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/comment")
public class CommentController {

    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private EntryRepository entryRepository;
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/all")
    public List<Comment> comments() {
        return this.commentRepository.findAll();
    }

    @GetMapping("/getComments/{eid}")
    public List<Comment> entryComments(@PathVariable String eid) {
        return this.commentRepository.findByEid_Eid(eid);
    }

    @PostMapping("/{eid}/{uuid}")
    public Object toComment(@PathVariable String eid, @PathVariable String uuid, @RequestBody Comment userComment) {
        Optional<Entry> optionalEntry = this.entryRepository.findByEid(eid);
        Optional<User> optionalUser = this.userRepository.findByUuid(uuid);
        if (optionalUser.isEmpty() || optionalEntry.isEmpty())
            return new ResponseStatusException(HttpStatus.NOT_FOUND, "Not Found");

        if (!optionalEntry.get().isOpenFComment()) new RuntimeException("Close to comment!");

        Comment comment = new Comment();
        comment.setContent(userComment.getContent());
        comment.setUid(optionalUser.get());
        comment.setEid(optionalEntry.get());
        return this.commentRepository.save(comment);
    }

    @PutMapping("")
    public Object updateComment(@RequestBody Comment userComment) {
        return this.commentRepository.save(userComment);
    }
}
