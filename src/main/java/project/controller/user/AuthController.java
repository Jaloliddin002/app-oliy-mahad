package project.controller.user;


import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import project.controller.BaseController;
import project.dto.request.UserLoginRequest;
import project.dto.request.UserRegisterRequest;
import project.exception.custom_ex.UserAlreadyRegisteredException;
import project.exception.custom_ex.UserAuthenticationException;
import project.security.jwt.payload.JwtResponse;
import project.service.user.oauth.OAuthService;

import javax.validation.Valid;

import static project.controller.BaseController.API;

@RestController
@RequiredArgsConstructor
@RequestMapping(API + "/user/auth")
@PreAuthorize(value = "permitAll()")
public class AuthController implements BaseController {

    private final OAuthService oAuthService;

    @GetMapping("/success")
    public ResponseEntity<?> signInSuccess (HttpServletResponse response){
        String accessToken = response.getHeader("access_token");
        String refreshToken = response.getHeader("refresh_token");

        return ResponseEntity.ok(
                new JwtResponse(HttpStatus.OK.value(),HttpStatus.OK.name(), accessToken)
        );
    }

    @PostMapping("/register")
    public ResponseEntity<?> register (
            @RequestBody @Valid UserRegisterRequest userRegisterRequest)
            throws UserAlreadyRegisteredException {

        JwtResponse jwtResponse = oAuthService.registerUser(userRegisterRequest);
        return ResponseEntity.ok(jwtResponse);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login (@RequestBody @Valid UserLoginRequest userLoginRequest)
            throws UserAuthenticationException{

        return ResponseEntity.ok(oAuthService.loginUser(userLoginRequest));
    }

}
