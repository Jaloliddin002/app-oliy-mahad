package project.controller.queue;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.controller.BaseController;
import project.dto.request.QueueDto;
import project.dto.response.RestAPIResponse;
import project.service.queue.QueueService;

import static project.controller.BaseController.API;


@RequiredArgsConstructor
@RestController
@RequestMapping(API + "/queue")
public class QueueController implements BaseController {

    private final QueueService queueService;

    @GetMapping(GET + "/getQueue")
    public RestAPIResponse getQueue (Pageable pageable) {
       return queueService.getQueueDetails(pageable);
    }

    @PostMapping(ADD)
    public ResponseEntity<?> addQueue(@RequestBody QueueDto queueDto) {
        RestAPIResponse apiResponse = queueService.create(queueDto);
        return ResponseEntity.status(apiResponse.isSuccess() ? HttpStatus.CREATED : HttpStatus.CONFLICT).body(apiResponse);
    }

    @GetMapping(GET + "/user_details/{queueId}")
    public ResponseEntity<?> getUserDetails (@PathVariable long queueId) {
        RestAPIResponse apiResponse = queueService.getUserDetails(queueId);
        return ResponseEntity.status(apiResponse.isSuccess() ? HttpStatus.OK : HttpStatus.NOT_FOUND).body(apiResponse);
    }

    @PutMapping(UPDATE + "/change_status/{id}")
    public ResponseEntity<?> changeStatus (@PathVariable long id, String status ) {
        RestAPIResponse apiResponse = queueService.changeQueueStatus(id,status);
        return ResponseEntity.status(apiResponse.isSuccess() ? HttpStatus.OK : HttpStatus.NOT_FOUND).body(apiResponse);
    }


    @GetMapping(GET)
    public ResponseEntity<?> getList (
          Pageable page
    ) {
        return ResponseEntity.ok(queueService.getList(page));
    }

    @GetMapping(GET+ "/{id}")
    public ResponseEntity<?> getQueue (@PathVariable Long id) {
        RestAPIResponse apiResponse = queueService.get(id);
        return ResponseEntity.status(apiResponse.isSuccess() ? HttpStatus.OK : HttpStatus.NOT_FOUND).body(apiResponse);
    }

    @PutMapping(UPDATE+ "/{id}")
    public ResponseEntity<?> updateQueue (@PathVariable Long id, @RequestBody QueueDto queueDto) {
        RestAPIResponse apiResponse = queueService.edit(id, queueDto);
        return ResponseEntity.status(apiResponse.isSuccess() ? HttpStatus.OK : HttpStatus.NOT_FOUND).body(apiResponse);
    }
    
    @DeleteMapping(DELETE + "/{id}")
    public ResponseEntity<?> deleteQueue (@PathVariable Long id) {
        RestAPIResponse apiResponse = queueService.delete(id);
        return ResponseEntity.status(apiResponse.isSuccess() ? HttpStatus.NO_CONTENT : HttpStatus.NOT_FOUND).body(apiResponse);
    }


    @GetMapping(GET_QUEUES_BY_FILTER)
    public ResponseEntity<?> getQueuesByFilter(
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) String gender,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) Long courseId,
            @RequestParam(required = false) String appliedDate
    ){
        RestAPIResponse queueByFilter = queueService.getQueueByFilter(userId, gender, status, courseId, appliedDate);
        return ResponseEntity.status(HttpStatus.OK).body(queueByFilter);
    }

    @GetMapping( GET + "/details")
    private ResponseEntity<?> getQueueDetails(
            @RequestParam(required = false, defaultValue = "0") Integer page,
            @RequestParam(required = false) Integer size,
            @RequestParam(required = false,defaultValue = "DESC") String order,
            @RequestParam(required = false) String[] tags
    ){
        return ResponseEntity.ok(
                (tags == null || tags.length == 0) ?
                        queueService.getQueueDetails(PageRequest.of(page, size)):
                        queueService.getQueueDetails(PageRequest.of(page, size, Sort.Direction.valueOf(order), tags))
                );
    }

}
