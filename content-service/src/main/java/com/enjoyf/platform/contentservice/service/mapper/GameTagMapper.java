package com.enjoyf.platform.contentservice.service.mapper;

import com.enjoyf.platform.contentservice.domain.game.GameTag;
import com.enjoyf.platform.contentservice.service.dto.GameTagDTO;
import com.enjoyf.platform.contentservice.service.dto.game.GameDTO;
import com.enjoyf.platform.contentservice.service.dto.game.GameSimpleDTO;
import com.enjoyf.platform.contentservice.web.rest.vm.GameTagVM;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * Created by zhimingli on 2017/6/21.
 */
@Mapper
public interface GameTagMapper {

    GameTagMapper MAPPER = Mappers.getMapper(GameTagMapper.class);


    GameTagDTO toGameTagDTO(GameTag gameTag);


    GameTagVM toGameTagVM(GameTag gameTag);

}
