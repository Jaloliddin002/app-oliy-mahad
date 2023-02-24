package project.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDetailsRegisterRequest {
    @NotBlank
    private Long userId;

    @Size(min = 3,max = 30)
    private String firstName;

    @Size(min = 3,max = 30)
    private String middleName;

    @Size(min = 3,max = 30)
    private String lastName;

    @NotBlank
    private String gender;

    @Size(min = 8, max = 24)
    private String passport;

    @NotBlank
    private Date birthDate;
}
