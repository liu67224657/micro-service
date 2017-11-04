package com.enjoyf.platform.contentservice.service.userservice.mapper;

import com.enjoyf.platform.contentservice.service.userservice.dto.UserProfileSimpleDTO;
import com.enjoyf.platform.contentservice.service.userservice.domain.UserProfile;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * Created by ericliu on 2017/8/15.
 */
@Mapper
public interface UserProfileMapper {

    UserProfileMapper MAPPER = Mappers.getMapper(UserProfileMapper.class);

    UserProfileSimpleDTO entity2SimpleDTO(UserProfile userProfile);
}
