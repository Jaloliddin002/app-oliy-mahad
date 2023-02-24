package project.service.user.oauth2;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import project.model.entity.user.UserEntity;
import project.repository.UserRepository;
import project.security.oauth2.UserPrincipal;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email)
            throws UsernameNotFoundException {
        Optional<UserEntity> optionalUserEntity = userRepository.findByEmail(email);
        UserEntity userEntity = optionalUserEntity
                .orElseThrow(() ->
                    new UsernameNotFoundException("User not found with email : " + email)
                );
        return UserPrincipal.create(userEntity);
    }
}
