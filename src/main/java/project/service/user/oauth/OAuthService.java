package project.service.user.oauth;


import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import project.dto.request.UserLoginRequest;
import project.dto.request.UserRegisterRequest;
import project.dto.response.UserDataResponse;
import project.exception.custom_ex.UserAlreadyRegisteredException;
import project.exception.custom_ex.UserInvalidPasswordException;
import project.model.entity.user.RoleEntity;
import project.model.entity.user.UserEntity;
import project.model.enums.ERole;
import project.repository.UserRepository;
import project.security.jwt.JWTokenProvider;
import project.security.jwt.payload.JwtResponse;

import java.util.HashSet;

@Service
@RequiredArgsConstructor
public class OAuthService {

    private final static Logger logger = LoggerFactory.getLogger(OAuthService.class);
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;
    private final UserDetailsService userDetailsService;
    private JWTokenProvider jwTokenProvider;

    public JwtResponse registerUser (UserRegisterRequest userRegisterRequest)
            throws UserAlreadyRegisteredException {
        UserEntity userEntity = new UserEntity();
        String token;
        try {
            userRegisterRequest.setPassword(passwordEncoder.encode(userRegisterRequest.getPassword()));
            userEntity.setRoles(new HashSet<>() {{
                add(new RoleEntity(1, ERole.ROLE_USER));
            }});
            modelMapper.map(userRegisterRequest,userEntity);
            UserEntity user = userRepository.save(userEntity);
            token = jwTokenProvider.generateAccessToken(user);
        }catch (
                IllegalArgumentException | ClassCastException | IllegalStateException |
                InvalidDataAccessApiUsageException e
        ){
            logger.error(e.getMessage());
            throw new IllegalArgumentException(e.getMessage());
        }catch (Exception e){
            logger.warn(e.getMessage());
            throw new UserAlreadyRegisteredException(
                    "User already registered with : " + userRegisterRequest.getPhoneNumber()
            );
        }
        return new JwtResponse(HttpStatus.OK.value(),HttpStatus.OK.name(),token);
    }

    public JwtResponse loginUser (UserLoginRequest userLoginRequest) {
        UserEntity userEntity = userRepository.findByPhoneNumber(userLoginRequest.getPhoneNumber())
                .orElseThrow(
                        () -> new UsernameNotFoundException(
                                "User not found with : " + userLoginRequest.getPhoneNumber()
                        )
                );
        if (passwordEncoder.matches(userLoginRequest.getPassword(), userEntity.getPassword())) {

            String token = jwTokenProvider.generateAccessToken(userEntity);

            return new JwtResponse(HttpStatus.OK.value(),HttpStatus.OK.name(),token);
        } else {
            throw new UserInvalidPasswordException("Wrong password");
        }
    }

//    public String[] validateRefreshToken (String  jwtRefreshToken) throws RuntimeException {
//        Jws<Claims> claimsJws =
//                jwTokenProvider.validateJwtRefreshToken(jwtRefreshToken);
//        UserEntity userEntity = userDetailsService.loadUserByUsername(claimsJws.getBody().getSubject());
//        return new String[]{jwTokenProvider.generateAccessToken(userEntity),jwtRefreshToken};
//    }


    public UserDataResponse getUser () {
        final UserEntity userEntity = (UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return modelMapper.map(userEntity, UserDataResponse.class);
    }

}
