package project.controller.course;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.controller.BaseController;
import project.dto.request.CourseDto;
import project.dto.response.RestAPIResponse;
import project.service.course.CourseService;

import static project.controller.BaseController.API;

@RestController
@RequestMapping(API + "/course" )
@RequiredArgsConstructor
public class CourseController implements BaseController {
    private final CourseService courseService;

    @PostMapping(ADD)
    public ResponseEntity<?> create(@RequestBody CourseDto courseDto) {
        RestAPIResponse apiResponse = courseService.create(courseDto);
        return ResponseEntity.status(apiResponse.isSuccess() ? HttpStatus.CREATED : HttpStatus.CONFLICT).body(apiResponse);
    }

    @GetMapping(GET + LIST)
    public ResponseEntity<?> getList (@RequestParam Pageable pageable) {
        return ResponseEntity.ok(courseService.getList(pageable));
    }

    @GetMapping(GET + "/{id}")
    public ResponseEntity<?> get(@PathVariable Long id) {
        RestAPIResponse apiResponse = courseService.get(id);
        return ResponseEntity.status(apiResponse.isSuccess() ? HttpStatus.OK : HttpStatus.NOT_FOUND).body(apiResponse);
    }

    @GetMapping(GET + "/{name}")
    public ResponseEntity<?> get(@RequestParam(name = "name") String name) {
        RestAPIResponse apiResponse = courseService.getByName(name);
        return ResponseEntity.status(apiResponse.isSuccess() ? HttpStatus.OK : HttpStatus.NOT_FOUND).body(apiResponse);
    }

    @PutMapping(UPDATE + "/{id}")
    public ResponseEntity<?> update (@PathVariable Long id, @RequestBody CourseDto courseDto){
        RestAPIResponse apiResponse = courseService.edit(id, courseDto);
        return ResponseEntity.status(apiResponse.isSuccess() ? HttpStatus.OK : HttpStatus.NOT_FOUND).body(apiResponse);
    }

    @DeleteMapping(DELETE + "/{id}")
    public ResponseEntity<?> delete (@PathVariable Long id) {
        RestAPIResponse apiResponse = courseService.delete(id);
        return ResponseEntity.status(apiResponse.isSuccess() ? HttpStatus.NO_CONTENT : HttpStatus.NOT_FOUND).body(apiResponse);
    }
}
