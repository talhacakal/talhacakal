package main.Model;

import jakarta.persistence.*;
import lombok.*;
import main.User.User;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
@Entity
@Table(name = "authorization")
public class Authorization {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "uuid", referencedColumnName = "uuid", nullable = false, unique = true)
    @NonNull
    private User uuid;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "authId", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "id", referencedColumnName = "id"))
    private Set<Role> roles = new HashSet<>();
    @Column(name = "create_date")
    @CreationTimestamp
    private LocalDateTime createDate;
    @Column(name = "last_update_date")
    @UpdateTimestamp
    private LocalDateTime lastUpdateDate;

    public void addRole(String role){
        getRoles().add(new Role(role));
    }

    public Authorization(@NonNull User uuid, Set<Role> roles) {
        this.uuid = uuid;
        this.roles = new HashSet<>();
    }
}
