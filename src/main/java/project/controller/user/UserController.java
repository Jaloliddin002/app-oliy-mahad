package project.controller.user;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import project.controller.BaseController;
import project.dto.request.UserUpdateRequest;
import project.dto.response.RestAPIResponse;
import project.service.user.UserService;
import project.service.user.oauth.OAuthService;

import javax.management.relation.RoleNotFoundException;
import javax.validation.Valid;

import static org.springframework.http.HttpStatus.OK;
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/user")
public class UserController implements BaseController {

    private final UserService userService;

    private final OAuthService oAuthService;

    //    @PreAuthorize(value = "hasAnyRole(\"ADMIN\",\"OWNER\")")
    @GetMapping()
    public ResponseEntity<?> userList(
            @RequestParam(name = "search",required = false) String search,
            @RequestParam(name = "categories", required = false) String[] categories,
            @RequestParam(name = "page", required = false, defaultValue = "0") Integer page,
            @RequestParam(name = "size", required = false, defaultValue = "20") Integer size,
            @RequestParam(name = "order",defaultValue = "DESC") String order

    ) {
        //TODO - DONE
        return ResponseEntity.ok(new RestAPIResponse(OK.name(),true, OK.value(), userService.list(
                search,
                categories,
                page,
                size,
                order
        )));
    }



    @PutMapping()
    public ResponseEntity<?> modifyUser(
            @RequestParam(required = true) Long id,
            @RequestBody @Valid UserUpdateRequest userUpdateRequest
    ){
        return ResponseEntity.ok(new RestAPIResponse(OK.name(), true, OK.value(),userService.updateUser(userUpdateRequest, id)));
    }


//    @GetMapping("/{phonenumber}")
//    public ResponseEntity<?> getUserByPhone(@PathVariable String phonenumber){
//        return ResponseEntity.ok( new RestAPIResponse(OK.name(), true , OK.value(),
//            userService.getByPhone(phonenumber)));
//
//    }

    @PutMapping("/{userId}/auth")
    @PreAuthorize(value = "hasAnyRole(\"ADMIN\")")
    public ResponseEntity<?> updateUserRole(
            @RequestParam(required = true) Integer roleId,
            @PathVariable Long userId
    ) throws RoleNotFoundException {
        return ResponseEntity.ok(
                new RestAPIResponse(
                        OK.name(),
                        true,
                        OK.value(),
                        userService.updateUserRole(userId, roleId)
                )
        );
    }
}
