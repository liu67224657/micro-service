package com.enjoyf.platform.contentservice.service.impl;

import com.enjoyf.platform.common.domain.enumeration.ValidStatus;
import com.enjoyf.platform.contentservice.repository.redis.RedisReplyHandleRepository;
import com.enjoyf.platform.contentservice.repository.redis.RedisReplyRepository;
import com.enjoyf.platform.contentservice.service.ReplyService;
import com.enjoyf.platform.contentservice.domain.reply.Reply;
import com.enjoyf.platform.contentservice.repository.jpa.ReplyRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Service Implementation for managing Reply.
 */
@Service
public class ReplyServiceImpl implements ReplyService {

    private final Logger log = LoggerFactory.getLogger(ReplyServiceImpl.class);

    private final ReplyRepository replyRepository;
    private final RedisReplyRepository redisReplyRepository;
    private final RedisReplyHandleRepository redisReplyHandleRepository;

    public ReplyServiceImpl(ReplyRepository replyRepository, RedisReplyRepository redisReplyRepository, RedisReplyHandleRepository redisReplyHandleRepository) {
        this.replyRepository = replyRepository;
        this.redisReplyRepository = redisReplyRepository;
        this.redisReplyHandleRepository = redisReplyHandleRepository;
    }

    /**
     * Save a reply.
     *
     * @param reply the entity to save
     * @return the persisted entity
     */
    @Override
    public Reply save(Reply reply) {
        log.debug("Request to save Reply : {}", reply);

        reply = replyRepository.save(reply);
        reply = redisReplyRepository.save(reply);

        //获取楼号，目前只有主楼有楼号
        long floorNum = 0l;
        if (reply.getRootId() == 0l) {
            floorNum = redisReplyHandleRepository.getReplyMaxFloorNum(reply.getReplyObjId());
        }
        reply.setFloorNum(floorNum);
        redisReplyHandleRepository.save(reply);
        return reply;
    }

    /**
     * Get all the replies.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Reply> findAll(Pageable pageable) {
        log.debug("Request to get all Replies");

        return replyRepository.findAll(pageable);
    }

    /**
     * Get one reply by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Reply findOne(Long id) {
        log.debug("Request to get Reply : {}", id);
        Reply reply = redisReplyRepository.findOne(id);
        if (reply == null) {
            reply = replyRepository.findOne(id);
            if (reply != null) {
                redisReplyRepository.save(reply);
            }
        }

        return reply;
    }

    /**
     * Delete the  reply by id.
     *
     * @param id the id of the entity
     */
    @Override
    public boolean delete(Long id) {
        log.debug("Request to delete Reply : {}", id);
        Reply reply = findOne(id);
        if (reply == null) {
            return false;
        }

        reply.setValidStatus(ValidStatus.UNVALID);
        replyRepository.save(reply);
        redisReplyRepository.delete(id);

        redisReplyHandleRepository.delReply(reply);

        return true;
    }
}
