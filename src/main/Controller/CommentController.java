package main.Controller;

import main.Model.Comment;
import main.Model.Entry;
import main.User.User;
import main.Repository.CommentRepository;
import main.Repository.EntryRepository;
import main.User.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.parameters.P;
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

    @PostMapping("/auth/{eid}")
    public Object toComment(@PathVariable String eid, @RequestBody Comment userComment, Authentication authentication) {
        Optional<Entry> optionalEntry = this.entryRepository.findByEid(eid);
        Optional<User> optionalUser = this.userRepository.findByEmail(authentication.getName());
        if (optionalEntry.isEmpty())
            return new ResponseStatusException(HttpStatus.NOT_FOUND, "Not Found");

        if (!optionalEntry.get().isOpenFComment()) new RuntimeException("Close to comment!");

        Comment comment = Comment.builder()
                .content(userComment.getContent())
                .uid(optionalUser.get())
                .eid(optionalEntry.get()).build();
//        comment.setContent(userComment.getContent());
//        comment.setUid(optionalUser.get());
//        comment.setEid(optionalEntry.get());
        return this.commentRepository.save(comment);
    }

    @PutMapping("/auth")
    @PreAuthorize(" hasAnyRole('ROLE_ADMIN') || authentication.name == @commentRepository.findByCid(#cid).get().getUid().getEmail()")
    public Object updateComment(@RequestParam String cid, @RequestParam String content) {
        Optional<Comment> optionalComment = this.commentRepository.findByCid(cid);
        optionalComment.get().setContent(content);

        return this.commentRepository.save(optionalComment.get());
    }

    @DeleteMapping("/auth")
    @PreAuthorize(" hasAnyRole('ROLE_ADMIN') || authentication.name == @commentRepository.findByCid(#cid).get().getUid().getEmail()")
    public Object deleteComment(@RequestParam String cid) {
        Optional<Comment> optionalComment = this.commentRepository.findByCid(cid);
        optionalComment.get().setUid(null);
        optionalComment.get().setEid(null);
        this.commentRepository.delete(optionalComment.get());
        return true;
    }

}
