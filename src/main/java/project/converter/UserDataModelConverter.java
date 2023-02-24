package project.converter;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import project.dto.response.UserDataResponse;
import project.model.entity.user.UserEntity;

import java.util.ArrayList;
import java.util.List;

public class UserDataModelConverter {

    private final static ModelMapper modelMapper = new ModelMapper();

    public static Page<UserDataResponse> converter (Page<UserEntity> source){
        List<UserDataResponse> list = source.getContent().size() > 0 ?
                source.getContent().stream().map(u -> modelMapper.map(u,UserDataResponse.class)).toList() :
                new ArrayList<>();
        return new PageImpl<>(list,source.getPageable(),source.getTotalPages());
    }

    public static List<UserDataResponse> convert (List<UserEntity> source) {
        List<UserDataResponse> list = source.size() > 0 ?
                source.stream().map(u -> modelMapper.map(u, UserDataResponse.class)).toList() :
                new ArrayList<>();
        return list;
    }

}
