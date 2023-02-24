
package project.service.user;


import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import project.model.entity.user.RoleEntity;
import project.model.enums.ERole;
import project.repository.RoleRepository;

@Service
@RequiredArgsConstructor
public class RoleService {

    private static final Logger logger = LoggerFactory.getLogger(RoleService.class);
    private final RoleRepository roleRepository;

    public RoleEntity addRole (String roleName) {
        try {
            return roleRepository.save(new RoleEntity(ERole.valueOf(roleName)));
        }catch (RuntimeException ex) {
            logger.error(ex.getMessage());
        }
        return null;
    }

}
