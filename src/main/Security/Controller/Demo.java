package main.Security.Controller;

import main.Repository.EntryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Demo {

    @Autowired
    private EntryRepository entryRepository;

    @GetMapping("/demo")
    public String hello(){
    return "sa";
    }

    @GetMapping("/info")
    public void getInfo(Authentication authentication){
        System.out.println(authentication.getName());
        System.out.println(authentication.getAuthorities());
        System.out.println(authentication.getPrincipal());
        System.out.println(authentication.getDetails());
    }

    @GetMapping("/getRole")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN') || " +
            "(!@entryRepository.findByEid(#eid).isEmpty() " +
            "&& authentication.name == @entryRepository.findByEid(#eid).get().getUid().getEmail())")
    public String getMyRoles(@P("eid") @RequestParam String eid, Authentication authentication){
        return "true";
    }

}
