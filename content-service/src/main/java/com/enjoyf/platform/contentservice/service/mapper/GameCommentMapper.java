package com.enjoyf.platform.contentservice.service.mapper;

import com.enjoyf.platform.contentservice.domain.gamecomment.GameComment;
import com.enjoyf.platform.contentservice.service.dto.gamecomment.GameCommentDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * Created by ericliu on 2017/8/15.
 */
@Mapper
public interface GameCommentMapper {

    GameCommentMapper MAPPER = Mappers.getMapper(GameCommentMapper.class);

    GameCommentDTO entity2DTO(GameComment gameComment);
}
