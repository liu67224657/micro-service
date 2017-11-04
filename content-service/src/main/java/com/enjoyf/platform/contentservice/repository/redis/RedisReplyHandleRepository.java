package com.enjoyf.platform.contentservice.repository.redis;

import com.enjoyf.platform.contentservice.domain.reply.Reply;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

/**
 * 评论的redis repository
 * Created by ericliu on 2017/9/11.
 */
@Component
public class RedisReplyHandleRepository extends AbstractRedis {

    private static final String PREFIX = "contentservice:gamecomment:";
    private static final String KEY_ZSET_REPLY_BY_OBJID = PREFIX + "z_reply_objid:";//主楼--添加到对应的OBJset中
    private static final String KEY_ZSET_REPLY_BY_ROOTID = PREFIX + "z_reply_rid:";//楼中楼添加到--ROOTID中
    private static final String KEY_ZSET_REPLY_BY_UID = PREFIX + "z_reply_uid:";//无论主楼还是字楼添加到--uid中

    private static final String KEY_HASH_REPLYOBJECT = PREFIX + "h_obj:";
    private static final String KEY_HASH_REPLYSUM = PREFIX + "h_replysum:";
    private static final String KEY_FOLLORNUM = "maxfollownum";//最大楼号主楼有用，楼中楼预留字段=评论数
    private static final String KEY_REPLYSUM = "replysum";//评论数，对OBJ来说：主楼+所有楼中楼；对楼中楼:该主楼下的楼中楼


    public RedisReplyHandleRepository(StringRedisTemplate redisTemplate) {
        super(redisTemplate);
    }

    /**
     * 获取主楼新的楼号
     *
     * @param objId 评论replyObjId
     * @return
     */
    public Long getReplyMaxFloorNum(String objId) {
        return redisTemplate.opsForHash().increment(KEY_HASH_REPLYOBJECT + objId, KEY_FOLLORNUM, 1);
    }

    /**
     * 获取子楼新楼号，暂时没用
     *
     * @param rootId 主楼id
     * @return
     */
    public Long getSubReplyMaxFloorNum(Long rootId) {
        return redisTemplate.opsForHash().increment(KEY_HASH_REPLYSUM + rootId, KEY_FOLLORNUM, 1);
    }

    /**
     * 主楼放在 用户列表，replyobj对应的列表。replyobj主楼数+1，replyobj评论数+1
     * 楼中楼放在 用户列表，rootReply对应的列表。rootReply主楼数+1，rootReply评论数+1，replyobj评论数+1
     * @param reply
     */
    public void save(Reply reply) {
        //判断是否是主楼
        if (reply.getRootId() == 0l) {
            //放入到评论（obj)列表中
            redisTemplate.opsForZSet().add(KEY_ZSET_REPLY_BY_OBJID + reply.getReplyObjId(), String.valueOf(reply.getId()), reply.getId());
        } else {
            //判断是否楼中楼
            //放入主楼（root)列表; 主楼（root）评论数+1
            redisTemplate.opsForZSet().add(KEY_ZSET_REPLY_BY_ROOTID + reply.getRootId(), String.valueOf(reply.getId()), reply.getId());
            redisTemplate.opsForHash().increment(KEY_HASH_REPLYSUM + reply.getRootId(), KEY_REPLYSUM, 1);
        }

        //通用 放入用户列表；增加评论数
        redisTemplate.opsForZSet().add(KEY_ZSET_REPLY_BY_UID + reply.getUid(), String.valueOf(reply.getId()), reply.getId());
        redisTemplate.opsForHash().increment(KEY_HASH_REPLYOBJECT + reply.getReplyObjId(), KEY_REPLYSUM, 1);
    }

    public Reply delReply(Reply reply) {
        //判断是否是主楼
        if (reply.getRootId() == 0l) {
            //放入到评论（obj)列表中
            redisTemplate.opsForZSet().remove(KEY_ZSET_REPLY_BY_OBJID + reply.getReplyObjId(), String.valueOf(reply.getId()));
        } else {
            //判断是否楼中楼
            //放入主楼（root)列表; 主楼（root）评论数+1
            redisTemplate.opsForZSet().remove(KEY_ZSET_REPLY_BY_ROOTID + reply.getRootId(), String.valueOf(reply.getId()));
            redisTemplate.opsForHash().increment(KEY_HASH_REPLYSUM + reply.getRootId(), KEY_REPLYSUM, -1);
        }

        //通用 放入用户列表；增加评论数
        redisTemplate.opsForZSet().remove(KEY_ZSET_REPLY_BY_UID + reply.getUid(), String.valueOf(reply.getId()), reply.getId());
        redisTemplate.opsForHash().increment(KEY_HASH_REPLYOBJECT + reply.getReplyObjId(), KEY_REPLYSUM, -1);
        return reply;
    }

}
