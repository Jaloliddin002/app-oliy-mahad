package project.service.section;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import project.dto.admin.AdminSectionDto;
import project.dto.admin.Section;
import project.dto.response.RestAPIResponse;
import project.dto.response.SectionPermissionDto;
import project.model.entity.sections.Sections;
import project.model.entity.user.UserEntity;
import project.repository.SectionRepository;
import project.repository.UserDetailRepository;
import project.repository.UserRepository;
import project.service.course.CourseService;
import project.service.group.GroupService;
import project.service.queue.QueueService;
import project.service.user.UserService;


import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AdminSectionService implements Section {

    private final SectionRepository sectionRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final UserDetailRepository userDetailRepository;
    private final CourseService courseService;
    private final GroupService groupService;
    private final QueueService queueService;
    private final UserService userService;

    private final static int ZERO = 0;


    public RestAPIResponse getSections(Long id, Pageable pageable) {
        Optional<Sections> optionalSections = sectionRepository.findById(id);
        if (optionalSections.isEmpty()) {
            return new RestAPIResponse("Finding item not found with : " + id, false, HttpStatus.NO_CONTENT.value());
        }
        Sections sections = optionalSections.get();
        AdminSectionDto adminSectionDto = new AdminSectionDto();
        switch (sections.getName()) {
            case USER:
                adminSectionDto = getUsers(pageable, sections);
                break;
            case GROUP:
                adminSectionDto = getGroup(pageable, sections);
                break;
            case COURSE:
                adminSectionDto = getCourse(pageable, sections);
                break;
            case QUEUE:
                adminSectionDto = getQueue(pageable, sections);
                break;
        }
        return new RestAPIResponse("Data list", true, HttpStatus.OK.value(), adminSectionDto);
    }

    public AdminSectionDto getUsers(Pageable pageable, Sections sections) {
        RestAPIResponse apiResponse = userService.getUsers(pageable);
        AdminSectionDto adminSectionDto = new AdminSectionDto();
        adminSectionDto.setHeaders(List.of("id", "firstName", "lastName", "middleName", "phoneNumber"));
        adminSectionDto.setBody(apiResponse.getData());
        modelMapper.map(getPermission(sections), adminSectionDto);
        return adminSectionDto;
    }

    public AdminSectionDto getCourse(Pageable pageable, Sections sections) {
        AdminSectionDto adminSectionDto = new AdminSectionDto();
        adminSectionDto.setHeaders(List.of("id", "name", "description", "price", "duration"));
        RestAPIResponse apiResponse = courseService.getList(pageable);
        adminSectionDto.setBody(apiResponse.getData());
        modelMapper.map(getPermission(sections), adminSectionDto);
        return adminSectionDto;
    }

    public AdminSectionDto getGroup(Pageable pageable, Sections sections) {
        AdminSectionDto adminSectionDto = new AdminSectionDto();
        adminSectionDto.setHeaders(List.of("id", "name", "memberCount", "startDate", "courseName", "courseId"));
        RestAPIResponse apiResponse = groupService.getGroups(pageable);
        adminSectionDto.setBody(apiResponse.getData());
        modelMapper.map(getPermission(sections), adminSectionDto);
        return adminSectionDto;
    }

    public AdminSectionDto getQueue(Pageable pageable, Sections sections) {
        AdminSectionDto adminSectionDto = new AdminSectionDto();
        adminSectionDto.setHeaders(List.of("id", "userId", "firstName", "lastName", "phoneNumber", "courseName", "appliedDate", "endDate"));
        RestAPIResponse apiResponse = queueService.getQueueDetails(pageable);
        adminSectionDto.setBody(apiResponse.getData());
        modelMapper.map(getPermission(sections), adminSectionDto);
        return adminSectionDto;
    }

    public Object getPermission(Sections sections) {
        SecurityContext context = SecurityContextHolder.getContext();
        UserEntity principal = (UserEntity) context.getAuthentication().getPrincipal();
        int number = principal.getRoles().stream().mapToInt(roleEntity -> (1 << roleEntity.getRoleName().getVal())).sum();
        SectionPermissionDto permissionDto = new SectionPermissionDto();
        if ((number & sections.getUpdate()) > ZERO) {
            permissionDto.setUpdate(true);
        }
        if ((number & sections.getInfo()) > ZERO) {
            permissionDto.setInfo(true);
        }
        if ((number & sections.getDelete()) > ZERO) {
            permissionDto.setDelete(true);
        }
        return permissionDto;
    }
}
