package com.enjoyf.platform.contentservice.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.enjoyf.platform.contentservice.domain.gamecomment.GameComment;
import com.enjoyf.platform.contentservice.service.GameService;
import com.enjoyf.platform.contentservice.service.UserCommentSumService;
import com.enjoyf.platform.contentservice.service.profileservice.ProfileServiceFeignClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URISyntaxException;

/**
 * Created by ericliu on 2017/8/16.
 */
@RestController
@RequestMapping("/api/tools/game-comments/")
public class ToolsGameCommentResource {


    private final Logger log = LoggerFactory.getLogger(ToolsGameCommentResource.class);

    private static final String ENTITY_NAME = "comment";

    private final UserCommentSumService userCommentSumService;
    private final GameService gameService;
    private final ProfileServiceFeignClient profileServiceFeignClient;

    public ToolsGameCommentResource(UserCommentSumService userCommentSumService, GameService gameService, ProfileServiceFeignClient profileServiceFeignClient) {
        this.userCommentSumService = userCommentSumService;
        this.gameService = gameService;
        this.profileServiceFeignClient = profileServiceFeignClient;
    }

    //todo
    @PostMapping
    @Timed
    public ResponseEntity<GameComment> createCommentByTools(@RequestBody GameComment comment) throws URISyntaxException {
//        log.debug("REST request to save Comment : {}", comment);
//        if (comment.getId() != null) {
//            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new comment cannot already have an ID")).body(null);
//        }//
//        if (comment.getScore() == 0 || comment.getGameId() == 0l || comment.getUid() == 0l) {
//            throw new CustomParameterizedException(ResultCodeConstants.PARAM_EMPTY.getExtmsg());
//        }
//        //游戏评分表
//        CommentRating commentRating = commentRatingService.findOneByGameId(comment.getGameId());
//        if (commentRating == null) {
//            throw new BusinessException(ResultCodeConstants.GAMEDB_GAME_NOTEXISTS.getExtmsg());
//        }
//
//        Comment getComment = gameCommentService.findGameIdAndUid(comment.getGameId(), comment.getUid());
//        if (getComment != null) {
//            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, ResultCodeConstants.COMMENT_HAS_EXIST.getMsg(), ResultCodeConstants.COMMENT_HAS_EXIST.getMsg())).body(null);
//        }
//        //真实评分
//        VerifyProfileDTO verifyProfileDTO = profileServiceFeignClient.findOne(comment.getUid());
//        comment.setReal_score(verifyProfileDTO != null && verifyProfileDTO.getVerifyType().equals(VerifyProfileType.VERIFY) ? comment.getScore() : comment.getScore() * 0.8);
//
//
//        GameComment gameComment = null;//todo
//
//
//        if (commentRating != null) {
//            //todo 逻辑修改
//            gameService.modifyGameRating(commentRating.getGameId(), gameComment.getRating(), 1);
//            //更新用户计数
//            userCommentSumService.increase(comment.getUid(), UserCommentSumFiled.COMMENT, 1);
//            commentRatingService.update(commentRating);//更新表评分信息
//        }
//        return ResponseEntity.created(new URI("/api/comments/" + gameComment.getId()))
//            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, gameComment.getId().toString()))
//            .body(gameComment);
        return null;
    }


}
