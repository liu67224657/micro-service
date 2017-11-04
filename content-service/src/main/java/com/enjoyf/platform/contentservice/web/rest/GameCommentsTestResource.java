package com.enjoyf.platform.contentservice.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.enjoyf.platform.contentservice.domain.gamecomment.GameComment;
import com.enjoyf.platform.contentservice.service.GameCommentService;
import com.enjoyf.platform.contentservice.service.facade.GameCommentFacade;
import com.enjoyf.platform.contentservice.service.userservice.UserProfileFeignClient;
import com.enjoyf.platform.contentservice.web.rest.util.AskUtil;
import com.enjoyf.platform.contentservice.web.rest.util.HeaderUtil;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Created by ericliu on 2017/8/16.
 */
@RestController
@RequestMapping("/api/game-comments/test")
public class GameCommentsTestResource {


    private final Logger log = LoggerFactory.getLogger(GameCommentsTestResource.class);

    private static final String ENTITY_NAME = "comment";

    private final GameCommentService gameCommentService;
    private final GameCommentFacade gameCommentFacade;

    private final UserProfileFeignClient userProfileFeignClient;
    private final AskUtil askUtil;

    public GameCommentsTestResource(GameCommentService gameCommentService, GameCommentFacade gameCommentFacade, UserProfileFeignClient userProfileFeignClient, AskUtil askUtil) {
        this.gameCommentService = gameCommentService;
        this.gameCommentFacade = gameCommentFacade;
        this.userProfileFeignClient = userProfileFeignClient;
        this.askUtil = askUtil;
    }

    @PostMapping
    @ApiOperation(value = "发布点评", response = GameComment.class)
    @Timed
    public String test() {

        for (long i = 1; i <= 30; i++) {
            GameComment gameComment = new GameComment();
            gameComment.setBody("这是一篇测试新的点评" + i);
            gameComment.setGameId(101184l);
            gameComment.setRating(4);
            gameComment.setRecommendValue(10);
            gameComment.setUid(i);
            gameComment = gameCommentFacade.postGameComment(gameComment);

            for (long j = 1; j <= 27; j++) {
                gameCommentFacade.agreeGameComment(j, gameComment.getId());
            }
        }

        return "success";
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "删除点评")
    @Timed
    public ResponseEntity<String> testdeleteComment(@PathVariable Long id) {
        log.debug("REST request to deleteComment Comment : {}", id);
        GameComment gameComment = gameCommentService.findOne(id);
        boolean bool = gameCommentFacade.deleteGameComment(gameComment.getUid(), id);

        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, String.valueOf(id))).body(bool ? "success" : "failed");
    }

}
