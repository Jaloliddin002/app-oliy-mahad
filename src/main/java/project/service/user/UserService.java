package project.service.user;


import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import project.converter.UserDataModelConverter;
import project.dto.admin.UserSectionDto;
import project.dto.request.ImageRequest;
import project.dto.request.UserUpdateRequest;
import project.dto.response.Response;
import project.dto.response.RestAPIResponse;
import project.dto.response.UserDataResponse;
import project.exception.custom_ex.UserNotFoundException;
import project.model.entity.user.UserEntity;
import project.model.entity.user.UserRegisterDetails;
import project.repository.UserDetailRepository;
import project.repository.UserRepository;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.*;

@Service
@RequiredArgsConstructor
public class UserService implements Response {

    private final static Logger logger = LoggerFactory.getLogger(UserService.class);
    private final UserRepository userRepository;
    private final UserDetailRepository userDetailRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    private final String baseImagePath = "C:\\Users\\99897\\IdeaProjects\\User-Section-of-Oliy-Mahad-project\\images\\avatar";

    public Page<?> list(String search, String[] categories, int page, int size, String order) {
        Page<UserEntity> list;
        if (categories == null && search == null)
            list = userRepository.findAll(
                    PageRequest.of(
                            page,
                            size
                    )
            );
        else if (categories != null && search == null) {
            list = userRepository.findAll(
                    PageRequest.of(
                            page,
                            size,
                            Sort.Direction.valueOf(order),
                            categories
                    ));
        } else if (categories == null)
            list = userRepository.
                    findAllByPhoneNumberContainingIgnoreCaseAndEmailContainingIgnoreCaseAndUsernameContainingIgnoreCase(
                            search,
                            search,
                            search,
                            PageRequest.of(page, size)
                    );

//        assert categories != null;
        else
            list = userRepository.
                    findAllByPhoneNumberContainingIgnoreCaseAndEmailContainingIgnoreCaseAndUsernameContainingIgnoreCase(
                            search,
                            search,
                            search,
                            PageRequest.of(
                                    page,
                                    size,
                                    Sort.Direction.valueOf(order),
                                    categories
                            )
                    );


        return list;
    }

    public UserDataResponse updateUser(UserUpdateRequest userUpdateRequest, long id) {
        UserEntity userEntity = userRepository.findById(id).orElseThrow(() -> {
            throw new UserNotFoundException("User not found with id - " + id);
        });
        if (userUpdateRequest.getPassword() != null) {
            userUpdateRequest.setPassword(passwordEncoder.encode(userUpdateRequest.getPassword()));
        }

        if (userUpdateRequest.getImage() != null) {
            String saveImage = imageSave(userUpdateRequest.getImage(), userEntity.getImageUrl());
            userEntity.setImageUrl(saveImage);
        }

        modelMapper.map(userUpdateRequest, userEntity);
        return modelMapper.map(userRepository.save(userEntity), UserDataResponse.class);

    }

    public RestAPIResponse getUsers (Pageable pageable) {
        Page<UserEntity> userEntities = userRepository.findAll(pageable);
        List<UserSectionDto> list = userEntities.getContent().size() > 0 ?
                userEntities.getContent().stream().map(u -> modelMapper.map(u, UserSectionDto.class)).toList() :
                new ArrayList<>();
        PageImpl<UserSectionDto> userSectionDtos = new PageImpl<>(list, userEntities.getPageable(), userEntities.getTotalPages());
        for (UserSectionDto userSectionDto : userSectionDtos) {
            Optional<UserRegisterDetails> optional = userDetailRepository.findByUserId(userSectionDto.getId());
            optional.ifPresent(userRegisterDetails -> modelMapper.map(userRegisterDetails, userSectionDto));
        }
        return new RestAPIResponse(USER,true,200,userSectionDtos);
    }

    public RestAPIResponse getUserDetails (long userId) {
        Optional<UserEntity> optionalUser = userRepository.findById(userId);
        if (optionalUser.isEmpty()) {
            return new RestAPIResponse(USER + NOT_FOUND, false,404);
        }
        UserEntity userEntity = optionalUser.get();
        Optional<UserRegisterDetails> optionalUserRegisterDetails = userDetailRepository.findByUserId(userEntity.getId());
        if (optionalUserRegisterDetails.isEmpty()){
            return new RestAPIResponse(NOT_FOUND,false,404);
        }
        UserRegisterDetails userRegisterDetails = optionalUserRegisterDetails.get();
        return new RestAPIResponse(USER,true,200,userRegisterDetails);
    }

    private String imageSave(ImageRequest imageRequest, String oldImageUrl) {
        byte[] de = Base64.getDecoder().decode(imageRequest.getContent());
        String uploadUrl = null;
        try {
            BufferedImage image = ImageIO.read(new ByteArrayInputStream(de));
            uploadUrl = baseImagePath + UUID.randomUUID() + "." + imageRequest.getContentType();
            File f = new File(uploadUrl);
            ImageIO.write(image, imageRequest.getContentType(), f);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (oldImageUrl != null) {
            File file = new File(oldImageUrl);
            file.delete();
        }


        return uploadUrl;
    }

    public List<UserDataResponse> getUsersByIds(List<Long> userIds) {
        if (userIds == null)
            throw new InputMismatchException("Users ids list is null");

        return UserDataModelConverter.convert(userRepository.findAllById(userIds));
    }

    public RestAPIResponse updateUserRole(Long userId, Integer roleId) {
        return null;
    }

}
