package com.enjoyf.platform.contentservice.repository.redis;

import com.enjoyf.platform.contentservice.domain.reply.Reply;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by ericliu on 2017/9/11.
 */
public interface RedisReplyRepository extends CrudRepository<Reply, Long> {
}
