package project.model.entity.user;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import project.model.enums.ERole;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "role")
public class RoleEntity implements GrantedAuthority {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique = true,nullable = false)
    @Enumerated(EnumType.STRING)
    private ERole roleName;


    public RoleEntity (ERole roleName) {this.roleName = roleName;}

    @Override
    public String getAuthority () {
        return roleName.name();
    }

}
