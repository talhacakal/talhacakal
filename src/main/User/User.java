package main.User;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
@Entity
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String uuid;
    @Column(unique = true)
    @NonNull
    private String email;
    @NonNull
    private String properties;
    @Column(name = "create_date")
    @CreationTimestamp
    private LocalDateTime createDate;
    @Column(name = "last_update_date")
    @UpdateTimestamp
    private LocalDateTime lastUpdateDate;

    @PrePersist
    protected void onCreate() {
        setUuid(String.valueOf(UUID.randomUUID()));
    }
}








//    @ManyToMany(fetch = FetchType.EAGER)
//    @JoinTable(
//            name = "user_roles",
//            joinColumns = @JoinColumn(name = "uid", referencedColumnName = "uid"),
//            inverseJoinColumns = @JoinColumn(name = "rid", referencedColumnName = "rid"))
//    private Set<Role> roles = new HashSet<>();
