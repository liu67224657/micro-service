package com.enjoyf.platform.contentservice.repository.redis;

import com.enjoyf.platform.contentservice.domain.enumeration.UserCommentSumFiled;
import com.enjoyf.platform.contentservice.domain.gamecomment.GameCommentSum;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhimingli on 2017/6/20.
 */
@Component
public class RedisGameCommentSumHandleRepository  extends AbstractRedis {

    private final String PREFIX_HASH_GAME_COMMENTSUM_ = "contentservice:gamecommentsum:";
    private final String AGREESUM = "agree";
    private final String REPLYSUM = "reply";

    public RedisGameCommentSumHandleRepository(StringRedisTemplate redisTemplate) {
        super(redisTemplate);
    }

    public GameCommentSum getGameCommentSum(long commentId) {
        GameCommentSum gameCommentSum = new GameCommentSum();
        gameCommentSum.setCommentId(commentId);
        List<Object> list = new ArrayList<>();
        list.add(AGREESUM);
        list.add(REPLYSUM);
        List valueList = redisTemplate.opsForHash().multiGet(getSumkey(commentId), list);
        gameCommentSum.setAgreeSum(valueList.get(0) == null ? 0 : Integer.parseInt((String) valueList.get(0)));
        gameCommentSum.setReplySum(valueList.get(1) == null ? 0 : Integer.parseInt((String) valueList.get(1)));
        return gameCommentSum;
    }


    public long increaseAgree(long commentId, Integer value) {
        return redisTemplate.opsForHash().increment(getSumkey(commentId), AGREESUM, value);
    }

    public long increaseReply(long commentId, Integer value) {
        return redisTemplate.opsForHash().increment(getSumkey(commentId), REPLYSUM, value);
    }

    private String getSumkey(long commentId) {
        return PREFIX_HASH_GAME_COMMENTSUM_ + commentId;
    }

}
