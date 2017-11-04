package com.enjoyf.platform.contentservice.repository.redis;

import com.enjoyf.platform.contentservice.domain.game.GameTag;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by zhimingli on 2017/6/20.
 */
public interface RedisGameTagRepository extends CrudRepository<GameTag, Long> {
    GameTag findByTagName(String tagName);

}
