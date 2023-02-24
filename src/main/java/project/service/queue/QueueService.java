package project.service.queue;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import project.dto.request.QueueDto;
import project.dto.response.QueueResponse;
import project.dto.response.Response;
import project.dto.response.RestAPIResponse;
import project.model.entity.course.CourseEntity;
import project.model.entity.queue.QueueEntity;
import project.model.entity.user.UserEntity;
import project.model.entity.user.UserRegisterDetails;
import project.model.enums.Status;
import project.repository.CourseRepository;
import project.repository.QueueRepository;
import project.repository.UserDetailRepository;
import project.repository.UserRepository;
import project.service.BaseService;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class QueueService implements BaseService<QueueDto,Long, Pageable>, Response {
    private final QueueRepository queueRepository;
    private final UserRepository userRepository;
    private final CourseRepository courseRepository;
    private final ModelMapper modelMapper;

    private final UserDetailRepository userDetailRepository;

    @Override
    public RestAPIResponse create(QueueDto queueDto) {
        Optional<UserEntity> optionalUser = userRepository.findById(queueDto.getUserId());
        if (optionalUser.isEmpty()){
            return new RestAPIResponse(USER + NOT_FOUND,true,404);
        }
        Optional<CourseEntity> optionalCourse = courseRepository.findById(queueDto.getCourseId());
        if (optionalCourse.isEmpty()) {
            return new RestAPIResponse(COURSE + NOT_FOUND , true, 404);
        }
        QueueEntity queueEntity = new QueueEntity();
        queueEntity.setUser(optionalUser.get());
        queueEntity.setCourse(optionalCourse.get());
        queueEntity.setStatus(Status.PENDING);
        queueRepository.save(queueEntity);
        return new RestAPIResponse(SUCCESSFULLY_SAVED, true,200);
    }

    @Override
    public RestAPIResponse getList(Pageable pageable) {
        return new RestAPIResponse(DATA_LIST,true,200);
    }

    @Override
    public RestAPIResponse get(Long id) {
        Optional<QueueEntity> optionalQueue = queueRepository.findById(id);
        if (optionalQueue.isEmpty()){
            return new RestAPIResponse(QUEUE + NOT_FOUND,false,404);
        }
        QueueDto queueDto = modelMapper.map(optionalQueue.get(), QueueDto.class);
        return new RestAPIResponse(QUEUE,true,200,queueDto);
    }

    @Override
    public RestAPIResponse edit(Long id, QueueDto queueDto) {
        Optional<QueueEntity> optionalQueue = queueRepository.findById(id);
        if (optionalQueue.isEmpty()) {
            return new RestAPIResponse(QUEUE + NOT_FOUND,false,404);
        }
        QueueEntity queueEntity = optionalQueue.get();
        if (queueDto.getAppliedDate() == null){
            queueDto.setAppliedDate(queueEntity.getAppliedDate());
        }
        modelMapper.map(queueDto,queueEntity);
        queueRepository.save(queueEntity);
        return new RestAPIResponse(SUCCESSFULLY_UPDATED,true,200);
    }

    @Override
    public RestAPIResponse delete(Long id) {
        Optional<QueueEntity> optionalQueue = queueRepository.findById(id);
        if (optionalQueue.isEmpty()){
            return new RestAPIResponse(QUEUE + NOT_FOUND,false,404);
        }
        queueRepository.delete(optionalQueue.get());
        return new RestAPIResponse(SUCCESSFULLY_DELETED,true,200);
    }
    public RestAPIResponse changeQueueStatus (Long id, String status){
        Optional<QueueEntity> optionalQueue = queueRepository.findById(id);
        if (optionalQueue.isEmpty()){
            return new RestAPIResponse(QUEUE + NOT_FOUND,false,404);
        }
        QueueEntity queueEntity = optionalQueue.get();
        queueEntity.setStatus(Status.valueOf(status));
        queueRepository.save(queueEntity);
        return new RestAPIResponse(SUCCESSFULLY_UPDATED,true, 200);
    }

    public RestAPIResponse getUserDetails (long queueId) {
        Optional<QueueEntity> optionalQueue = queueRepository.findById(queueId);
        if (optionalQueue.isEmpty()) {
            return new RestAPIResponse(QUEUE + NOT_FOUND,false,404);
        }
        QueueEntity queueEntity = optionalQueue.get();
        UserEntity userEntity = queueEntity.getUser();
        Optional<UserRegisterDetails> optional = userDetailRepository.findByUserId(userEntity.getId());
        if (optional.isEmpty()){
            return new RestAPIResponse(USER + NOT_FOUND,false,404);
        }
        UserRegisterDetails userRegisterDetails = optional.get();
        return new RestAPIResponse(USER,true,200,userRegisterDetails);
    }

    public List<UserEntity> getUsers (long courseId, String status, int limit, String gender) {
        List<QueueEntity> queueEntities = queueRepository.filterByCourseStatusGenderLimitForGroups(courseId,status,gender,limit);
        List<UserEntity> users = new ArrayList<>();
        for (QueueEntity queue : queueEntities) {
            if (queue.getStatus().equals(Status.PENDING)) {
                users.add(queue.getUser());
                queue.setStatus(Status.ACCEPT);
                queueRepository.save(queue);
            }
        }
        return users;
    }


    public RestAPIResponse getQueueDetails (Pageable pageable) {
        Page<QueueEntity> queueEntities = queueRepository.findAllByStatus("PENDING",pageable);
        List<QueueResponse> list = queueEntities.getContent().size() > 0 ?
                queueEntities.getContent().stream().map(u ->
                        modelMapper.map(u, QueueResponse.class)).toList() :
                new ArrayList<>();
        for (QueueResponse queueResponse : list) {
            for (QueueEntity queueEntity : queueEntities) {
                if (queueResponse.getId().equals(queueEntity.getId())) {
                    UserEntity user = queueEntity.getUser();
                    Optional<UserRegisterDetails> optional = userDetailRepository.findByUserId(user.getId());
                    if (optional.isEmpty()){
                        return new RestAPIResponse(NOT_FOUND,false,404);
                    }
                    UserRegisterDetails userRegisterDetails = optional.get();
                    queueResponse.setEmail(user.getEmail());
                    queueResponse.setFirstName(userRegisterDetails.getFirstName());
                    queueResponse.setPhoneNumber(user.getPhoneNumber());
                    queueResponse.setLastName(userRegisterDetails.getLastName());
                    queueResponse.setUserId(user.getId());
                    CourseEntity course = queueEntity.getCourse();
                    queueResponse.setCourseName(course.getName());
                    queueResponse.setStatus(queueEntity.getStatus());
                    queueResponse.setAppliedDate(queueEntity.getAppliedDate());
                }
            }
        }
        PageImpl<QueueResponse> queueResponses = new PageImpl<>(list, queueEntities.getPageable(), queueEntities.getTotalPages());
        return new RestAPIResponse(USER,true,200,queueResponses);

    }

    private String getDayAfterDay(String day) {
        String sDay = day.substring(0, 10);
        Date date = null;
        try {
            date = new SimpleDateFormat("yyyy-MM-dd").parse(sDay);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long l = date.getTime() + 86400000;
        Date date1 = new Date(l);
        String afterDay = new SimpleDateFormat("yyyy-MM-dd").format(date1);
        return afterDay;
    }

    public RestAPIResponse getQueueByFilter(Long userId, String gender, String status, Long courseId, String appliedDate) {
        String appliedDateAfter = null;
        if (appliedDate != null) {
            appliedDateAfter = getDayAfterDay(appliedDate);
        }
        List<QueueEntity> queueByFilter = queueRepository.getQueueByFilter(userId, gender, status, courseId);
        return new RestAPIResponse(SUCCESS, true, 200, queueByFilter);

    }
}
