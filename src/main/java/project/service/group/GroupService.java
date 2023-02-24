package project.service.group;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import project.dto.admin.GroupSectionDto;
import project.dto.request.GroupRequestDto;
import project.dto.response.Response;
import project.dto.response.RestAPIResponse;
import project.model.entity.course.CourseEntity;
import project.model.entity.group.GroupEntity;
import project.model.entity.user.UserEntity;
import project.model.enums.GroupStatusEnum;
import project.repository.CourseRepository;
import project.repository.GroupRepository;
import project.repository.QueueRepository;
import project.service.BaseService;
import project.service.course.CourseService;
import project.service.queue.QueueService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GroupService implements  Response {
    private final GroupRepository groupRepository;
    private final CourseRepository courseRepository;
    private final QueueService queueService;
    private final QueueRepository queueRepository;
    private final ModelMapper modelMapper;

    public RestAPIResponse create(GroupRequestDto groupRequestDto) {
        Optional<CourseEntity> optionalCourse = courseRepository.findById(groupRequestDto.getCourseId());
        if (optionalCourse.isEmpty()) {
            return new RestAPIResponse(COURSE + NOT_FOUND,false,404);
        }
        GroupEntity groupEntity = modelMapper.map(groupRequestDto, GroupEntity.class);
        groupEntity.setCourse(optionalCourse.get());
        groupEntity.setGroupStatus(GroupStatusEnum.IN_PROGRESS);
        groupEntity.setUserEntities(queueService.getUsers(optionalCourse.get().getId(),
                "PENDING",groupRequestDto.getMembersCount(),groupRequestDto.getGender()));
        groupRepository.save(groupEntity);
        return new RestAPIResponse(SUCCESSFULLY_SAVED,true,200);
    }
    public RestAPIResponse getGroups(Pageable pageable) {
        final Page<GroupEntity> entityPage = groupRepository.findAll(pageable);
        final List<GroupSectionDto> list = entityPage.getContent().size() > 0  ?
                entityPage.getContent().stream().map(u -> modelMapper.map(u,GroupSectionDto.class)).toList()
                : new ArrayList<>();
        PageImpl<GroupSectionDto> sectionDtoPage = new PageImpl<>(list,entityPage.getPageable(),entityPage.getTotalPages());
        return new RestAPIResponse(DATA_LIST,true,200,sectionDtoPage);
    }
    public RestAPIResponse getGroupUsers (Long id) {
        Optional<GroupEntity> optionalGroup = groupRepository.findById(id);
        if (optionalGroup.isEmpty()) {
            return new RestAPIResponse(GROUP + NOT_FOUND,false,404);
        }
        List<UserEntity> userEntities = optionalGroup.get().getUserEntities();
        return new RestAPIResponse(DATA_LIST,true,200,userEntities);
    }
    public RestAPIResponse updateGroup (Long id, GroupRequestDto groupRequestDto){
        Optional<GroupEntity> optionalGroup = groupRepository.findById(id);
        if (optionalGroup.isEmpty()){
            return new RestAPIResponse(GROUP + NOT_FOUND,false,404);
        }
        GroupEntity groupEntity = optionalGroup.get();
        modelMapper.map(groupRequestDto,groupEntity);
        groupRepository.save(groupEntity);
        return new RestAPIResponse(SUCCESSFULLY_UPDATED,true,200);
    }

}
