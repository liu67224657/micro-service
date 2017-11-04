package com.enjoyf.platform.contentservice.service.mapper;

import com.enjoyf.platform.contentservice.domain.game.Game;
import com.enjoyf.platform.contentservice.domain.game.GameSum;
import com.enjoyf.platform.contentservice.service.dto.game.GameDTO;
import com.enjoyf.platform.contentservice.service.dto.game.GameSimpleDTO;
import com.enjoyf.platform.contentservice.service.dto.game.GameSumDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

/**
 * Created by ericliu on 2017/8/15.
 */
@Mapper
public interface GameMapper {

    GameMapper MAPPER = Mappers.getMapper(GameMapper.class);

    GameSumDTO entity2GameSumDTO(GameSum gameSum);
    GameDTO simple2GameDTO(GameSimpleDTO game);
}
