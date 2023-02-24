package project.model.entity.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import project.audit.Auditable;
import project.model.enums.EGender;

import java.util.Date;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "user_register_details",
        indexes = {
                @Index(name = "firstName", columnList = "firstName DESC"),
                @Index(name = "middleName", columnList = "middleName DESC"),
                @Index(name = "lastName", columnList = "lastName DESC"),
                @Index(name = "birthDate", columnList = "birthDate DESC"),
        }
)
public class UserRegisterDetails extends Auditable<String> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @OneToOne(fetch = FetchType.LAZY)
    private UserEntity user;

    private String firstName;

    private String middleName;

    private String lastName;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private EGender gender;

    private String password;
    private Date birthDate;

}
