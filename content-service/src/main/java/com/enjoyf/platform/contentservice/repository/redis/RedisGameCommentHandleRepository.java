package com.enjoyf.platform.contentservice.repository.redis;

import com.enjoyf.platform.common.util.StringUtil;
import com.enjoyf.platform.contentservice.domain.gamecomment.GameComment;
import com.enjoyf.platform.contentservice.service.CommentIdQueryType;
import com.enjoyf.platform.page.ScoreRange;
import com.google.common.base.Strings;
import io.jsonwebtoken.lang.Collections;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by ericliu on 2017/8/16.
 */
@Repository
public class RedisGameCommentHandleRepository extends AbstractRedis {
    private static final String PREFIX = "contentservice:gamecomment:";
    private static final String KEY_COMMENT_HOT_ = PREFIX + ":commenthotzset:";//点评列表 最热
    private static final String KEY_COMMENT_NEW_ = PREFIX + ":commentnewzset:";//点评列表 最新

    private static final String KEY_USER_COMMENT_ = PREFIX + "usercommentzset:";//我的点评列表
    private static final String KEY_USER_COMMENT_GAME_ = PREFIX + "usercomentgamezset:";//我的点评过的游戏列表
    private static final String KEY_UIDGID_2_COMMENTID = PREFIX + "uidgid2commentid:";//我的点评过的游戏列表


    public RedisGameCommentHandleRepository(StringRedisTemplate redisTemplate) {
        super(redisTemplate);
    }

    public void addCommentIdByGameId(GameComment comment) {
        if (StringUtil.isEmpty(comment.getBody())) {
            return;
        }
        String hotMilli = "0." + comment.getId();
        redisTemplate.opsForZSet().add(KEY_COMMENT_NEW_ + comment.getGameId(), String.valueOf(comment.getId()), comment.getId());
        redisTemplate.opsForZSet().add(KEY_COMMENT_HOT_ + comment.getGameId(), String.valueOf(comment.getId()), Double.valueOf(hotMilli + "1"));
    }

    public List<Long> queryCommentIdByGameIds(Long gameId, ScoreRange scoreRange, CommentIdQueryType type) {
        String key = "";
        if (type.equals(CommentIdQueryType.HOT)) {
            key = KEY_COMMENT_NEW_ + gameId;
        } else {
            key = KEY_COMMENT_NEW_ + gameId;
        }

        Set<String> idStrs = zrangeByScore(key, scoreRange);
        if (Collections.isEmpty(idStrs)) {
            return new ArrayList<>();
        }

        List<Long> result = new ArrayList<>();
        idStrs.forEach(id -> {
            try {
                result.add(Long.parseLong(id));
            } catch (NumberFormatException e) {
            }
        });
        return result;
    }


    public void removeCommentIdByGameId(GameComment comment) {
        redisTemplate.opsForZSet().remove(KEY_COMMENT_NEW_ + comment.getGameId(), String.valueOf(comment.getId()));
        redisTemplate.opsForZSet().remove(KEY_COMMENT_HOT_ + comment.getGameId(), String.valueOf(comment.getId()));
    }

    public void addCommentByUser(GameComment comment) {
        long milli = comment.getCreateTime().toInstant().toEpochMilli();
        redisTemplate.opsForZSet().add(KEY_USER_COMMENT_ + comment.getUid(), String.valueOf(comment.getId()), milli);
        redisTemplate.opsForZSet().add(KEY_USER_COMMENT_GAME_ + comment.getUid(), String.valueOf(comment.getGameId()), milli);
        redisTemplate.opsForValue().set(KEY_UIDGID_2_COMMENTID + comment.getUid() + comment.getGameId(), String.valueOf(comment.getId()));
    }

    public void removeCommentByUser(GameComment comment) {
        redisTemplate.opsForZSet().remove(KEY_USER_COMMENT_ + comment.getUid(), String.valueOf(comment.getId()));
        redisTemplate.opsForZSet().remove(KEY_USER_COMMENT_GAME_ + comment.getUid(), String.valueOf(comment.getGameId()));
        redisTemplate.delete(KEY_UIDGID_2_COMMENTID + comment.getUid() + comment.getGameId());
    }


    public Long getCommentIdByUidGameId(Long uid, Long gameId) {
        String val = redisTemplate.opsForValue().get(KEY_UIDGID_2_COMMENTID + uid + gameId);

        if (Strings.isNullOrEmpty(val)) {
            return null;
        }
        return Long.parseLong(val);
    }

    /**
     * 查询用户点评过的游戏
     *
     * @param uid     user id
     * @param gameIds find gameids
     * @return gameIds return  commented game ids
     */
    public Set<Long> checkGameIsComment(long uid, Set<Long> gameIds) {
        Set<Long> returnMap = new LinkedHashSet<>();
        gameIds.forEach(gameId -> {
            Double score = redisTemplate.opsForZSet().score(KEY_USER_COMMENT_GAME_ + uid, String.valueOf(gameId));
            if (score != null) {
                returnMap.add(gameId);
            }
        });
        return returnMap;
    }


}
