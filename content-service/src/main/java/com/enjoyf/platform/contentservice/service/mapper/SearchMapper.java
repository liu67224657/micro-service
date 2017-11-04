package com.enjoyf.platform.contentservice.service.mapper;

import com.enjoyf.platform.contentservice.domain.game.Game;
import com.enjoyf.platform.contentservice.service.dto.search.GameSearchDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

/**
 * Created by ericliu on 2017/8/21.
 */
@Mapper
public interface SearchMapper {
    SearchMapper MAPPER = Mappers.getMapper(SearchMapper.class);

    GameSearchDTO game2SearchDTO(Game game);
}
