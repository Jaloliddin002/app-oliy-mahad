package project.controller.group;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.controller.BaseController;
import project.dto.request.GroupRequestDto;
import project.dto.response.RestAPIResponse;
import project.service.group.GroupService;

import static project.controller.BaseController.API;

@RestController
@RequestMapping(API + "/group")
@RequiredArgsConstructor
public class GroupController implements BaseController {
    private final GroupService groupService;

    @RequestMapping(ADD)
    public ResponseEntity<?> create (@RequestBody GroupRequestDto groupRequestDto){
        RestAPIResponse restAPIResponse = groupService.create(groupRequestDto);
        return ResponseEntity.status(restAPIResponse.isSuccess() ?
                HttpStatus.CREATED : HttpStatus.CONFLICT).body(restAPIResponse);
    }

    @RequestMapping(GET + LIST)
    public ResponseEntity<?> getList (Pageable pageable){
        return ResponseEntity.ok(groupService.getGroups(pageable));
    }


    @RequestMapping(GET + "/user_details/{userId}")
    public ResponseEntity<?> getUserDetails (@PathVariable long userId) {
        RestAPIResponse apiResponse = groupService.getGroupUsers((userId));
        return ResponseEntity.status(apiResponse.isSuccess() ? HttpStatus.OK : HttpStatus.NOT_FOUND).body(apiResponse);
    }

    @GetMapping(GET+"/{id}")
    public ResponseEntity<?> getGroupUsers (@PathVariable Long id) {
        RestAPIResponse apiResponse = groupService.getGroupUsers(id);
        return ResponseEntity.status(apiResponse.isSuccess() ? HttpStatus.OK : HttpStatus.NOT_FOUND).body(apiResponse);
    }

    @PutMapping(UPDATE + "/{id}")
    public ResponseEntity<?> updateGroup (@PathVariable long id, @RequestBody GroupRequestDto groupRequestDto) {
        RestAPIResponse apiResponse = groupService.updateGroup(id, groupRequestDto);
        return ResponseEntity.status(apiResponse.isSuccess() ? HttpStatus.OK : HttpStatus.NOT_FOUND).body(apiResponse);
    }





}
