package project.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.model.entity.user.UserEntity;
import project.model.entity.user.UserRegisterDetails;

import java.util.Optional;

@Repository
public interface UserDetailRepository extends JpaRepository<UserRegisterDetails, Long> {
    Optional<UserRegisterDetails> findByUser (UserEntity user);
    Optional<UserRegisterDetails> findByUserId(Long user_id);
}
