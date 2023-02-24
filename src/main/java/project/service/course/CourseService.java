package project.service.course;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import project.dto.admin.CourseSectionDto;
import project.dto.request.CourseDto;
import project.dto.response.Response;
import project.dto.response.RestAPIResponse;
import project.model.entity.course.CourseEntity;
import project.repository.CourseRepository;
import project.service.BaseService;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CourseService implements BaseService<CourseDto, Long, Pageable>, Response {

    private final CourseRepository courseRepository;
    private final ModelMapper modelMapper;


    @Override
    public RestAPIResponse create(CourseDto courseDto) {
        boolean exists = courseRepository.existsByName(courseDto.getName());
        if (exists) {
            return new RestAPIResponse(COURSE + ALREADY_EXIST,false,404);
        }
        CourseEntity course = modelMapper.map(courseDto, CourseEntity.class);
        courseRepository.save(course);
        return new RestAPIResponse(SUCCESSFULLY_SAVED,true,200);
    }

    @Override
    public RestAPIResponse getList(Pageable pageable) {
        Page<CourseEntity> courseEntities = courseRepository.findAll(pageable);
        List<CourseSectionDto> list = courseEntities.getContent().size() > 0 ?
                courseEntities.getContent().stream().map(u -> modelMapper.map(u, CourseSectionDto.class)).toList() :
                new ArrayList<>();
        PageImpl<CourseSectionDto> courseSectionDtos = new PageImpl<>(list,courseEntities.getPageable(),courseEntities.getTotalPages());
        return new RestAPIResponse(COURSE + DATA_LIST,true,200,courseSectionDtos);
    }

    @Override
    public RestAPIResponse get(Long id) {
        Optional<CourseEntity> optionalCourse = courseRepository.findById(id);
        if (optionalCourse.isEmpty()){
            return new RestAPIResponse(COURSE + NOT_FOUND, false,404);
        }
        CourseSectionDto courseSectionDto = modelMapper.map(optionalCourse.get(), CourseSectionDto.class);
        return new RestAPIResponse(COURSE,true,200,courseSectionDto);
    }

    @Override
    public RestAPIResponse edit(Long id, CourseDto courseDto) {
        Optional<CourseEntity> optionalCourse = courseRepository.findById(id);
        if (optionalCourse.isEmpty()) {
            return new RestAPIResponse(COURSE + NOT_FOUND,false,404);
        }
        CourseEntity courseEntity = optionalCourse.get();
        modelMapper.map(courseDto,courseEntity);
        courseRepository.save(courseEntity);
        return new RestAPIResponse(SUCCESSFULLY_UPDATED,true,200);
    }

    @Override
    public RestAPIResponse delete(Long id) {
        Optional<CourseEntity> optionalCourse = courseRepository.findById(id);
        if (optionalCourse.isEmpty()){
            return new RestAPIResponse(COURSE + NOT_FOUND,false,404);
        }
        CourseEntity courseEntity = optionalCourse.get();
        courseRepository.delete(courseEntity);
        return new RestAPIResponse(SUCCESSFULLY_DELETED,true,201);
    }

    public RestAPIResponse getByName (String name){
        Optional<CourseEntity> optionalCourseEntity = courseRepository.findByName(name);
        if (optionalCourseEntity.isEmpty()){
            return new RestAPIResponse(COURSE + NOT_FOUND,false,404);
        }
        CourseEntity courseEntity = optionalCourseEntity.get();
        CourseSectionDto courseSectionDto = modelMapper.map(courseEntity, CourseSectionDto.class);
        return new RestAPIResponse(COURSE,true,200,courseSectionDto);
    }
}
