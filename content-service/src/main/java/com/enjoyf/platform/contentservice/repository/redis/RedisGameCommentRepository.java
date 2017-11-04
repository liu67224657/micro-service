package com.enjoyf.platform.contentservice.repository.redis;

import com.enjoyf.platform.contentservice.domain.gamecomment.GameComment;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by ericliu on 2017/8/16.
 */
public interface RedisGameCommentRepository extends CrudRepository<GameComment, Long> {

}
