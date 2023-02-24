package project.controller.section;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.controller.BaseController;
import project.dto.request.SectionRequestDto;
import project.dto.response.RestAPIResponse;
import project.service.section.AdminSectionService;
import project.service.section.SectionService;

import javax.validation.Valid;

@RestController
@RequestMapping("api/v1/user/admin/section")
@RequiredArgsConstructor
public class SectionController implements BaseController {
    private final SectionService sectionService;
    private final AdminSectionService adminSectionService;

    @PostMapping("/edit")
    public Boolean addSection(@RequestBody  @Valid SectionRequestDto sectionRequestDto) {
        sectionService.addSection(sectionRequestDto);
        return true;
    }

    @GetMapping()
    public ResponseEntity<?> getAccessForSections() {
        return ResponseEntity.ok(sectionService.getAccessForSections());
    }
    @GetMapping("/get")
    public ResponseEntity<?> getSections() {
        return  ResponseEntity.ok(sectionService.getList());
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<?> getSectionById ( @PathVariable Long id) {
        RestAPIResponse restAPIResponse = sectionService.getSection(id);
        return ResponseEntity.status(restAPIResponse.isSuccess() ? HttpStatus.OK : HttpStatus.NOT_FOUND).body(restAPIResponse);
    }

    @GetMapping("/data")
    public ResponseEntity<?> getSections(
            @RequestParam Long id,
             Pageable pageable
    ){
        return ResponseEntity.ok().body(adminSectionService.getSections(id,pageable));
    }
}
