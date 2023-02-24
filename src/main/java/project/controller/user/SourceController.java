package project.controller.user;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import project.dto.request.RoleRegisterRequest;
import project.service.user.RoleService;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/user/admin/")
@RequiredArgsConstructor
public class SourceController {

    private final RoleService roleService;

    @PostMapping("/role_expansion")
    public ResponseEntity<?> addRole (@RequestBody @Valid RoleRegisterRequest request){
        return ResponseEntity.ok(roleService.addRole(request.getRoleName()));
    }
}
