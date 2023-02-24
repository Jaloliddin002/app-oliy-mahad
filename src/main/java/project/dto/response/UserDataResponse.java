package project.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import project.model.entity.user.RoleEntity;
import project.model.entity.user.UserRegisterDetails;
import project.model.enums.EAuthProvider;

import java.sql.Timestamp;
import java.util.Set;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDataResponse {

    private Long id;

    private String username;

    private String phoneNumber;

    private String email;

    private EAuthProvider provider;

    private String imageUrl;

    private Set<RoleEntity> roles;

    private UserRegisterDetails userRegisterDetails;

    private Timestamp createdAt;

}