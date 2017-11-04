package com.enjoyf.platform.contentservice.service.impl;

import com.enjoyf.platform.contentservice.domain.gamecomment.enumeration.GameCommentOperType;
import com.enjoyf.platform.contentservice.domain.gamecomment.GameComment;
import com.enjoyf.platform.contentservice.domain.gamecomment.GameCommentOperation;
import com.enjoyf.platform.contentservice.domain.gamecomment.GameCommentSum;
import com.enjoyf.platform.contentservice.repository.jpa.GameCommentOperationRepository;
import com.enjoyf.platform.contentservice.repository.jpa.GameCommentRepository;
import com.enjoyf.platform.contentservice.repository.redis.RedisGameCommentRepository;
import com.enjoyf.platform.contentservice.repository.redis.RedisGameCommentOperationHandleRepository;
import com.enjoyf.platform.contentservice.repository.redis.RedisGameCommentHandleRepository;
import com.enjoyf.platform.contentservice.repository.redis.RedisGameCommentSumHandleRepository;
import com.enjoyf.platform.contentservice.service.CommentIdQueryType;
import com.enjoyf.platform.contentservice.service.GameCommentService;
import com.enjoyf.platform.contentservice.service.userservice.dto.UserProfileSimpleDTO;
import com.enjoyf.platform.contentservice.service.dto.gamecomment.GameCommentDTO;
import com.enjoyf.platform.contentservice.service.dto.gamecomment.GameCommentInfoDTO;
import com.enjoyf.platform.contentservice.service.mapper.GameCommentMapper;
import com.enjoyf.platform.contentservice.service.userservice.UserProfileFeignClient;
import com.enjoyf.platform.contentservice.service.userservice.domain.UserProfile;
import com.enjoyf.platform.contentservice.service.userservice.mapper.UserProfileMapper;
import com.enjoyf.platform.page.ScoreRange;
import com.enjoyf.platform.page.ScoreRangeRows;
import io.jsonwebtoken.lang.Collections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Service Interface for managing CommentRating.
 */
@Service
public class GameCommentServiceImpl implements GameCommentService {
    private final Logger log = LoggerFactory.getLogger(CommentOperationServiceImpl.class);

    private final GameCommentRepository gameCommentRepository;
    private final RedisGameCommentHandleRepository redisGameCommentHandleRepository;
    private final RedisGameCommentRepository redisGameCommentRepository;

    private final GameCommentOperationRepository gameCommentOperationRepository;
    private final RedisGameCommentOperationHandleRepository redisGameCommentOperationHandleRepository;

    private final RedisGameCommentSumHandleRepository redisGameCommentSumHandleRepository;

    private final UserProfileFeignClient userProfileFeignClient;

    public GameCommentServiceImpl(GameCommentRepository gameCommentRepository, RedisGameCommentHandleRepository redisGameCommentHandleRepository, RedisGameCommentRepository redisGameCommentRepository, GameCommentOperationRepository gameCommentOperationRepository, RedisGameCommentOperationHandleRepository redisGameCommentOperationHandleRepository, RedisGameCommentSumHandleRepository redisGameCommentSumHandleRepository, UserProfileFeignClient userProfileFeignClient) {
        this.gameCommentRepository = gameCommentRepository;
        this.redisGameCommentHandleRepository = redisGameCommentHandleRepository;
        this.redisGameCommentRepository = redisGameCommentRepository;
        this.gameCommentOperationRepository = gameCommentOperationRepository;
        this.redisGameCommentOperationHandleRepository = redisGameCommentOperationHandleRepository;
        this.redisGameCommentSumHandleRepository = redisGameCommentSumHandleRepository;
        this.userProfileFeignClient = userProfileFeignClient;
    }

    @Override
    public GameComment save(GameComment gameComment) {
        log.debug("Request to save Comment : {}", gameComment);
        GameComment result = gameCommentRepository.save(gameComment);
        redisGameCommentRepository.save(result);

        if (result.getId() > 0) {
            redisGameCommentHandleRepository.addCommentIdByGameId(gameComment);
            redisGameCommentHandleRepository.addCommentByUser(gameComment);
        }
        return result;
    }

    @Override
    public GameComment update(GameComment gameComment) {
        log.debug("Request to update Comment : {}", gameComment);

        GameComment result = gameCommentRepository.save(gameComment);
        redisGameCommentRepository.save(result);
        return gameComment;
    }

    @Override
    public GameComment findByUIdAndGameId(Long uid, Long gameId) {
        log.debug("Request to findByUIdAndGameId uid : {}, gameId: {}", uid, gameId);

        Long commentId = redisGameCommentHandleRepository.getCommentIdByUidGameId(uid, gameId);
        if (commentId == null) {
            GameComment gameComment = gameCommentRepository.findOneByUidAndGameId(uid, gameId);
            if (gameComment != null) {
                commentId = gameComment.getId();
            }
        }

        if (commentId == null) {
            return null;
        }
        return findOne(commentId);
    }

    @Override
    public Page<GameComment> findAll(Pageable pageable) {
        return null;
    }

    @Override
    public GameComment findOne(Long id) {
        log.debug("Request to findOne id: {}", id);

        GameComment gameComment = redisGameCommentRepository.findOne(id);
        if (gameComment == null) {
            gameComment = gameCommentRepository.findOne(id);

            if (gameComment != null) {
                gameCommentRepository.save(gameComment);
            }
        }
        return gameComment;
    }

    @Override
    public boolean delete(GameComment gameComment) {
        log.debug("Request to delete gameComment: {}, id: {}");

        gameCommentRepository.delete(gameComment);
        redisGameCommentRepository.delete(gameComment);

        redisGameCommentHandleRepository.removeCommentIdByGameId(gameComment);
        redisGameCommentHandleRepository.removeCommentByUser(gameComment);

        return true;
    }

    @Override
    public boolean agree(GameCommentOperation operation) {
        log.debug("Request to delete operation: {}", operation);

        boolean bval = redisGameCommentOperationHandleRepository.hasOperation(operation.getCommentId(), operation.getOperateType(), operation.getUid());
        if (bval) {
            return false;
        }
        gameCommentOperationRepository.save(operation);
        redisGameCommentOperationHandleRepository.addOperation(operation);

        if (GameCommentOperType.AGREE.equals(operation.getOperateType())) {
            redisGameCommentSumHandleRepository.increaseAgree(operation.getCommentId(), 1);
        }

        return true;
    }

    @Override
    public boolean delAgree(Long uid, Long commentId) {
        log.debug("Request to delete uid: {}, commentId: {}", uid, commentId);

        boolean bval = redisGameCommentOperationHandleRepository.hasOperation(commentId, GameCommentOperType.AGREE, uid);
        if (!bval) {
            return false;
        }
        List<GameCommentOperation> list = gameCommentOperationRepository.findByCommentIdAndUid(commentId, uid);

        gameCommentOperationRepository.delete(list.get(0));
        redisGameCommentOperationHandleRepository.removeOperation(commentId, GameCommentOperType.AGREE, uid);

        redisGameCommentSumHandleRepository.increaseAgree(commentId, -1);

        return true;
    }

    @Override
    public ScoreRangeRows<GameCommentInfoDTO> findByGameId(Long gameId, Long uid, ScoreRange scoreRange, CommentIdQueryType type) {
        log.debug("Request to findByGameId, gameId: {}.uid: {}", uid, uid);

        ScoreRangeRows<GameCommentInfoDTO> result = new ScoreRangeRows<>();
        List<Long> idList = redisGameCommentHandleRepository.queryCommentIdByGameIds(gameId, scoreRange, type);
        result.setRange(scoreRange);
        if (Collections.isEmpty(idList)) {
            return result;
        }

        List<GameComment> gameCommentList = findByGameCommentIds(idList);
        if (Collections.isEmpty(gameCommentList)) {
            return result;
        }

        Set<Long> uidQuerySet = new HashSet<>();
        gameCommentList.forEach(comment -> uidQuerySet.add(comment.getUid()));

        //查询用户信息
        List<UserProfile> userProfiles = userProfileFeignClient.findUserProfilesByUids(uidQuerySet.toArray(new Long[]{}));
        Map<Long, UserProfile> map = new HashMap<>();
        userProfiles.forEach((userProfile -> map.put(userProfile.getId(), userProfile)));

        List<GameCommentInfoDTO> returnCommentDTO = new ArrayList<>();
        gameCommentList.forEach(gameComment -> {
            UserProfile userProfile = map.get(gameComment.getUid());
            Boolean hasAgree = (uid != null && uid != 0) && redisGameCommentOperationHandleRepository.hasOperation(gameComment.getId(), GameCommentOperType.AGREE, uid);
            GameCommentSum gameCommentSum = redisGameCommentSumHandleRepository.getGameCommentSum(gameComment.getId());

            GameCommentInfoDTO commentDTO = buildCommentDTO(gameComment, gameCommentSum, userProfile, hasAgree);
            returnCommentDTO.add(commentDTO);
        });
        result.setRows(returnCommentDTO);

        return result;
    }

    private GameCommentInfoDTO buildCommentDTO(GameComment gameComment, GameCommentSum gameCommentSum, UserProfile userProfile, Boolean hasAgree) {
        GameCommentDTO commentDTO = GameCommentMapper.MAPPER.entity2DTO(gameComment);
        UserProfileSimpleDTO userProfileSimpleDTO = UserProfileMapper.MAPPER.entity2SimpleDTO(userProfile);

        GameCommentInfoDTO result = new GameCommentInfoDTO();
        result.setComment(commentDTO);
        result.setSum(gameCommentSum);
        result.setHasAgree(hasAgree);
        result.setProfile(userProfileSimpleDTO);
        return result;
    }

    private List<GameComment> findByGameCommentIds(List<Long> idList) {
        Map<Long, GameComment> map = new HashMap<>();
        List<Long> queryDbSet = new ArrayList<>();
        for (Long id : idList) {
            GameComment gameComment = redisGameCommentRepository.findOne(id);
            if (gameComment != null) {
                map.put(id, gameComment);
            } else {
                queryDbSet.add(id);
            }
        }

        if (!Collections.isEmpty(queryDbSet)) {
            List<GameComment> gameCommentDBList = gameCommentRepository.findAllByIdIn(queryDbSet);

            gameCommentDBList.forEach(gameComment -> {
                redisGameCommentRepository.save(gameComment);
                map.put(gameComment.getId(), gameComment);
            });
        }

        List<GameComment> result = new ArrayList<>();
        idList.forEach(id -> {
            if (map.containsKey(id)) {
                result.add(map.get(id));
            }
        });
        return result;
    }

}
