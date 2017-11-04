package com.enjoyf.platform.contentservice.service.facade;

import com.enjoyf.platform.autoconfigure.web.error.BusinessException;
import com.enjoyf.platform.common.ResultCodeConstants;
import com.enjoyf.platform.common.util.StringUtil;
import com.enjoyf.platform.contentservice.domain.gamecomment.enumeration.GameCommentOperType;
import com.enjoyf.platform.contentservice.domain.enumeration.UserCommentSumFiled;
import com.enjoyf.platform.contentservice.domain.gamecomment.GameComment;
import com.enjoyf.platform.contentservice.domain.gamecomment.GameCommentOperation;
import com.enjoyf.platform.contentservice.service.CommentIdQueryType;
import com.enjoyf.platform.contentservice.service.GameCommentService;
import com.enjoyf.platform.contentservice.service.GameService;
import com.enjoyf.platform.contentservice.service.UserCommentSumService;
import com.enjoyf.platform.contentservice.service.dto.gamecomment.GameCommentInfoDTO;
import com.enjoyf.platform.page.ScoreRange;
import com.enjoyf.platform.page.ScoreRangeRows;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.Locale;

/**
 * Created by ericliu on 2017/8/16.
 */
@Service
public class GameCommentFacade {
    private final GameCommentService gameCommentService;
    private final GameService gameService;
    private final UserCommentSumService userCommentSumService;
    private final MessageSource messageSource;

    public GameCommentFacade(GameCommentService gameCommentService, GameService gameService, UserCommentSumService userCommentSumService, MessageSource messageSource) {
        this.gameCommentService = gameCommentService;
        this.gameService = gameService;
        this.userCommentSumService = userCommentSumService;
        this.messageSource = messageSource;
    }

    public GameComment postGameComment(GameComment gameComment) {
        gameComment.setCreateTime(ZonedDateTime.now());
        gameComment = gameCommentService.save(gameComment);

        if (gameComment.getRating() != 0) {
            gameService.modifyGameRating(gameComment.getGameId(), gameComment.getRating(), 1);
        }

        if (gameComment.getRecommendValue() != 0) {
            gameService.modifyGameRecommend(gameComment.getGameId(), gameComment.getRecommendValue(), 1);
        }

        userCommentSumService.increase(gameComment.getUid(), UserCommentSumFiled.COMMENT, 1);
        return gameComment;
    }

    public GameComment updateGameCommentValue(Long commentId,
                                              Long uid,
                                              Integer rating,
                                              Integer recommendValue,
                                              String body) throws BusinessException {
        GameComment gameComment = gameCommentService.findOne(commentId);
        if (gameComment == null) {
            throw new BusinessException("ID不存在", String.valueOf(commentId));
        }
        if (gameComment.getUid() != uid) {
            throw new BusinessException(ResultCodeConstants.USER_NOT_LOGIN.getMsg(), ResultCodeConstants.USER_NOT_LOGIN.getExtmsg());
        }

        int oldRating = gameComment.getRating();
        int oldRecommend = gameComment.getRecommendValue();

        if (rating != null && rating > 0) {
            gameComment.setRating(rating);
        }

        if (recommendValue != null && recommendValue > 0) {
            gameComment.setRecommendValue(recommendValue);
        }

        if (!StringUtil.isEmpty(body)) {
            gameComment.setBody(body);
        }

        gameComment.setModifyTime(ZonedDateTime.now());
        gameCommentService.update(gameComment);

        //有变化
        int changeValue = rating - oldRating;
        if (changeValue != 0) {
            gameService.modifyGameRating(gameComment.getGameId(), rating - oldRating, 0);
        }
        int changeRecommendValue = recommendValue - oldRecommend;
        if (changeRecommendValue != 0) {
            gameService.modifyGameRecommend(gameComment.getGameId(), recommendValue - oldRecommend, 0);
        }

        return gameComment;
    }

    public boolean deleteGameComment(Long uid, Long id) {
        GameComment gameComment = gameCommentService.findOne(id);
        if (gameComment == null) {
            throw new BusinessException(getMessage("gamecomment.notexistis", null), "");
        }

        if (uid != gameComment.getUid()) {
            throw new BusinessException(getMessage("gamecomment.notbelongto.user", null), "");
        }

        boolean bool = gameCommentService.delete(gameComment);
        if (bool) {
            if (gameComment.getRating() != 0) {
                gameService.modifyGameRating(gameComment.getGameId(), -gameComment.getRating(), -1);
            }
            if (gameComment.getRecommendValue() != 0) {
                gameService.modifyGameRecommend(gameComment.getGameId(), -gameComment.getRecommendValue(), -1);
            }
        }
        return bool;
    }

    public boolean agreeGameComment(Long uid, Long id) {
        GameComment comment = gameCommentService.findOne(id);
        if (comment == null) {
            throw new BusinessException(getMessage("gamecomment.notexistis", null));
        }

        GameCommentOperation operation = new GameCommentOperation();
        operation.setCommentId(id);
        operation.setDestUid(comment.getUid());
        operation.setOperateType(GameCommentOperType.AGREE);
        operation.setUid(uid);

        return gameCommentService.agree(operation);
    }

    public boolean delAgreeGameComment(Long uid, Long commentId) {
        GameComment comment = gameCommentService.findOne(commentId);
        if (comment == null) {
            throw new BusinessException(getMessage("gamecomment.notexistis", null));
        }

        return gameCommentService.delAgree(uid, commentId);
    }

    public ScoreRangeRows<GameCommentInfoDTO> findByGameId(Long uid, Long gameId, ScoreRange scoreRange, CommentIdQueryType queryType) {
        return gameCommentService.findByGameId(gameId, uid, scoreRange, queryType);
    }

    private String getMessage(String key, Object[] param) {
        return messageSource.getMessage(key, param, Locale.CHINA);
    }
}
