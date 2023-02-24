package project.repository;

import org.apache.catalina.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.model.entity.user.RoleEntity;
import project.model.entity.user.UserEntity;

import java.util.Optional;
import java.util.Set;

@Repository
public interface UserRepository extends JpaRepository<UserEntity,Long> {

    Optional<UserEntity> findByEmail(String email);

    Optional<UserEntity> findByUsername(String username);

    Optional<UserEntity> findByPhoneNumber(String phoneNumber);

    Page<UserEntity> findByRoles(Integer roleId, Pageable pageable);

    Page<UserEntity> findAllByPhoneNumberContainingIgnoreCaseAndEmailContainingIgnoreCaseAndUsernameContainingIgnoreCase(
            String phoneNumber,
            String email,
            String username,
            Pageable pageable
    );

}
