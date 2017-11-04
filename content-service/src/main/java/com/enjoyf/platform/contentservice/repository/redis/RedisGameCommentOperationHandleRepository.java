package com.enjoyf.platform.contentservice.repository.redis;

import com.enjoyf.platform.contentservice.domain.gamecomment.enumeration.GameCommentOperType;
import com.enjoyf.platform.contentservice.domain.gamecomment.GameCommentOperation;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

/**
 * Created by ericliu on 2017/8/16.
 */
@Repository
public class RedisGameCommentOperationHandleRepository {
    private static final String PREFIX = "contentservice:gamecommentoperation:";

    private static final String KEY_OPERATION_UID_ZSET = PREFIX + "uidzset:";


    private final StringRedisTemplate redisTemplate;

    public RedisGameCommentOperationHandleRepository(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void addOperation(GameCommentOperation gameCommentOperation) {
        redisTemplate.opsForZSet().add(getUidZsetKey(gameCommentOperation.getCommentId(), gameCommentOperation.getOperateType()), String.valueOf(gameCommentOperation.getUid()), gameCommentOperation.getId());
    }

    public boolean hasOperation(Long commentId, GameCommentOperType type, Long uid) {
        return redisTemplate.opsForZSet().score(getUidZsetKey(commentId, type), String.valueOf(uid)) != null;
    }

    public boolean removeOperation(Long commentId, GameCommentOperType type, Long uid) {
        return redisTemplate.opsForZSet().remove(getUidZsetKey(commentId, type), String.valueOf(uid)) > 0;
    }


    private String getUidZsetKey(Long commentId, GameCommentOperType type) {
        return KEY_OPERATION_UID_ZSET + commentId + type.ordinal();
    }
}
