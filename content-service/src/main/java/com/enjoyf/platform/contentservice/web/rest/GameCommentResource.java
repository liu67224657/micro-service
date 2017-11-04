package com.enjoyf.platform.contentservice.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.enjoyf.platform.autoconfigure.security.EnjoySecurityUtils;
import com.enjoyf.platform.autoconfigure.web.error.BusinessException;
import com.enjoyf.platform.common.ResultCodeConstants;
import com.enjoyf.platform.common.domain.enumeration.ValidStatus;
import com.enjoyf.platform.contentservice.domain.gamecomment.GameComment;
import com.enjoyf.platform.contentservice.service.CommentIdQueryType;
import com.enjoyf.platform.contentservice.service.GameCommentService;
import com.enjoyf.platform.contentservice.service.dto.gamecomment.GameCommentInfoDTO;
import com.enjoyf.platform.contentservice.service.facade.GameCommentFacade;
import com.enjoyf.platform.contentservice.service.userservice.UserProfileFeignClient;
import com.enjoyf.platform.contentservice.service.userservice.domain.UserProfile;
import com.enjoyf.platform.contentservice.web.rest.util.AskUtil;
import com.enjoyf.platform.contentservice.web.rest.util.HeaderUtil;
import com.enjoyf.platform.page.ScoreRange;
import com.enjoyf.platform.page.ScoreRangeRows;
import com.enjoyf.platform.page.ScoreSort;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * game comments api
 * Created by ericliu on 2017/8/16.
 */
@RestController
@RequestMapping("/api/game-comments")
public class GameCommentResource extends AbstractBaseResource {


    private final Logger log = LoggerFactory.getLogger(GameCommentResource.class);

    private static final String ENTITY_NAME = "comment";

    private final GameCommentService gameCommentService;
    private final GameCommentFacade gameCommentFacade;

    private final UserProfileFeignClient userProfileFeignClient;
    private final AskUtil askUtil;

    public GameCommentResource(GameCommentService gameCommentService, GameCommentFacade gameCommentFacade, UserProfileFeignClient userProfileFeignClient, AskUtil askUtil) {
        this.gameCommentService = gameCommentService;
        this.gameCommentFacade = gameCommentFacade;
        this.userProfileFeignClient = userProfileFeignClient;
        this.askUtil = askUtil;
    }

    /**
     * POST   : Create a new comment.
     *
     * @param gameComment the comment to create
     * @return the ResponseEntity with status 201 (Created) and with body the new comment, or with status 400 (Bad Request) if the comment has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping
    @ApiOperation(value = "发布点评", response = GameComment.class)
    @Timed
    public ResponseEntity<GameComment> createComment(@RequestBody GameComment gameComment) throws URISyntaxException {
        log.debug("REST request to save Comment : {}", gameComment);
        if (gameComment.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new comment cannot already have an ID")).body(null);
        }

        //校验
        Long currentUid = EnjoySecurityUtils.getCurrentUid();
        if (gameComment.getRating() == 0 || gameComment.getGameId() == 0l || currentUid == 0l) {
            throw new BusinessException(getMessage("param.empty", null));
        }

        UserProfile userProfile = userProfileFeignClient.findOne(currentUid);
        if (userProfile == null) {
            throw new BusinessException(ResultCodeConstants.USERCENTER_PROFILE_NOT_EXISTS.getMsg(), ResultCodeConstants.USERCENTER_PROFILE_NOT_EXISTS.getExtmsg());
        }

        //
        boolean bool = askUtil.checkUserForbidStatus(userProfile.getProfileNo());
        if (!bool) {
            throw new BusinessException(ResultCodeConstants.COMMENT_PROFILE_FORBID.getExtmsg(), ResultCodeConstants.COMMENT_PROFILE_FORBID.getExtmsg());
        }

        GameComment hasComment = gameCommentService.findByUIdAndGameId(currentUid, gameComment.getGameId());
        if (hasComment != null && ValidStatus.VALID.equals(hasComment.getValidStatus())) {
            throw new BusinessException(ResultCodeConstants.COMMENT_HAS_EXIST.getExtmsg(), ResultCodeConstants.COMMENT_HAS_EXIST.getExtmsg());
        }

        if (hasComment != null) {
            gameComment.setId(hasComment.getId());
        }
        gameComment.setUid(currentUid);
        gameCommentFacade.postGameComment(gameComment);

        return ResponseEntity.created(new URI("/api/game-comments/" + gameComment.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, gameComment.getId().toString()))
            .body(gameComment);
    }


    /**
     * PUT  /comments : Updates an existing comment.
     *
     * @return the ResponseEntity with status 200 (OK) and with body the updated comment,
     * or with status 400 (Bad Request) if the comment is not valid,
     * or with status 500 (Internal Server Error) if the comment couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping
    @ApiOperation(value = "修改点评", response = GameComment.class)
    @Timed
    public ResponseEntity<GameComment> updateComment(@RequestParam Long id,
                                                     @RequestParam(required = false) Integer rating,
                                                     @RequestParam(required = false) Integer recommend,
                                                     @RequestParam(required = false) String body) throws URISyntaxException {
        log.debug("REST request to update Comment : {}", id);
        //校验
        if (rating == null || rating == 0) {
            //评分不能为空
            throw new BusinessException(ResultCodeConstants.SCORE_IS_NOT_NULL.getExtmsg(), ResultCodeConstants.SCORE_IS_NOT_NULL.getExtmsg());
        }
        Long uid = EnjoySecurityUtils.getCurrentUid();
        if (uid == null || uid == 0) {
            throw new BusinessException(ResultCodeConstants.USER_NOT_LOGIN.getMsg(), ResultCodeConstants.USER_NOT_LOGIN.getExtmsg());
        }

        GameComment gameComment = gameCommentFacade.updateGameCommentValue(id, uid, rating, recommend, body);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, gameComment.getId().toString()))
            .body(gameComment);
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "删除点评")
    @Timed
    public ResponseEntity<String> deleteComment(@PathVariable Long id) {
        log.debug("REST request to deleteComment Comment : {}", id);
        Long uid = EnjoySecurityUtils.getCurrentUid();
        if (uid == null || uid == 0) {
            throw new BusinessException(getMessage("user.not.login", null));
        }

        boolean bool = gameCommentFacade.deleteGameComment(uid, id);

        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, String.valueOf(id))).body(bool ? "success" : "failed");
    }

    @PostMapping("/agree/{id}")
    @ApiOperation(value = "点赞", response = Boolean.class)
    @Timed
    public ResponseEntity<String> agree(@PathVariable Long id) {
        log.debug("REST request to agreeComment Comment : {}", id);
        Long uid = EnjoySecurityUtils.getCurrentUid();
        if (uid == null || uid == 0) {
            throw new BusinessException(getMessage("user.not.login", null), null);
        }

        boolean bool = gameCommentFacade.agreeGameComment(uid, id);

        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, String.valueOf(id))).body(bool ? "success" : "failed");
    }

    @DeleteMapping("/agree/{id}")
    @ApiOperation(value = "取消点赞", response = Boolean.class)
    @Timed
    public ResponseEntity<String> delAgree(@PathVariable Long id) {
        log.debug("REST request to agreeComment Comment : {}", id);
        Long uid = EnjoySecurityUtils.getCurrentUid();
        if (uid == null || uid == 0) {
            throw new BusinessException(getMessage("user.not.login", null), null);
        }

        boolean bool = gameCommentFacade.delAgreeGameComment(uid, id);

        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, String.valueOf(id))).body(bool ? "success" : "failed");
    }

    @GetMapping("/{gameId}")
    @ApiOperation(value = "按照游戏ID获取点评列表", response = Boolean.class)
    @Timed
    public ResponseEntity<ScoreRangeRows> queryCommentByGameId(@PathVariable Long gameId,
                                                               @RequestParam(value = "psize", defaultValue = "15") Integer pageSize,
                                                               @RequestParam(name = "flag", defaultValue = "-1") Double flag,
                                                               @RequestParam(name = "type", defaultValue = "HOT") CommentIdQueryType type) {
        log.debug("REST request to queryCommentByGameId Comment : {}", gameId);
        try {

            Long currentUid = EnjoySecurityUtils.getCurrentUid();
            ScoreRange scoreRange = new ScoreRange(-1, flag, pageSize, ScoreSort.DESC);

            ScoreRangeRows<GameCommentInfoDTO> dtoScoreRangeRows = gameCommentFacade.findByGameId(currentUid, gameId, scoreRange, type);

            return ResponseEntity.ok(dtoScoreRangeRows);
        } catch (Exception e) {
            log.error("queryCommentByGameId.error.e ", e);
            throw new BusinessException("system.error", "");
        }
    }

//    /**
//     * GET  /comments : get all the comments. todo
//     *
//     * @return the ResponseEntity with status 200 (OK) and the list of comments in body
//     */
//    @GetMapping("/comments")
//    @Timed
//    public ResponseEntity<List<CommentDTO>> getAllComments(@ApiParam Pageable pageable,
//                                                           @RequestParam(value = "validStatus", defaultValue = "valid") String validStatus,
//                                                           @RequestParam(value = "gameId", defaultValue = "0") Long gameId,
//                                                           @RequestParam(value = "id", defaultValue = "0") Long id,
//                                                           @RequestParam(value = "body", defaultValue = "") String body) {
//        log.debug("REST request to get a page of Comments");
////        Page<Comment> page = gameCommentService.findAll(validStatus, gameId, id, body, pageable);
////        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/comments");
////        List<CommentDTO> returnList = new ArrayList<>();
////        if (!CollectionUtil.isEmpty(page.getContent())) {
////            returnList = askUtil.queryCommentDTOByParam(page.getContent());
////        }
////        return new ResponseEntity<>(returnList, headers, HttpStatus.OK);
//        return null;
//    }

//    @GetMapping("/comments/detail/{id}")
//    @ApiOperation(value = "点评详情", response = CommentDTO.class)
//    @Timed
//    public ResponseEntity<CommentDetailDTO> getCommentDetail(@PathVariable Long id) {
//        log.debug("REST request to getCommentDetail Comment : {}", id);
//
//        Long uid = EnjoySecurityUtils.getCurrentUid();
//        CommentDetailDTO commentDetailDTO = gameCommentService.getCommentDetailById(id, uid);
//        return new ResponseEntity<>(commentDetailDTO, HttpStatus.OK);
//    }
//

//
//    @GetMapping("/comments/mycomments")
//    @ApiOperation(value = "我的点评")
//    @Timed
//    public ResponseEntity<ScoreRangeRows> queryCommentByGameId(@RequestParam(value = "psize", defaultValue = "15") Integer pageSize,
//                                                               @RequestParam(value = "flag", defaultValue = "-1") Double flag,
//                                                               @RequestParam(value = "uid", required = false, defaultValue = "0") Long uid) {
//        log.debug("REST request to queryCommentByGameId Comment : {}");
//        ScoreRangeRows<CommentDetailDTO> scoreRangeRows = new ScoreRangeRows<>();
//        if (uid == null || uid == 0) {
//            throw new BusinessException(ameCommentFacade.getMessage("user.not.login",null));
//        }
//        Long currentUid = EnjoySecurityUtils.getCurrentUid();
//        ScoreRange scoreRange = new ScoreRange(-1, flag, pageSize, ScoreSort.DESC);
//        scoreRangeRows = gameCommentService.queryMyComments(uid, scoreRange, currentUid);
//        scoreRangeRows.setTotalRows(commentRedisRepository.getUserCommentCount(uid).intValue());
//        return new ResponseEntity<>(scoreRangeRows, HttpStatus.OK);
//    }
//

//
//
//    @PutMapping("/comments/tools/{id}")
//    public ResponseEntity<Boolean> modifyComments(@PathVariable Long id,
//                                                  @RequestParam(value = "status", required = false) String status,
//                                                  @RequestParam(value = "uid", required = false) String uid,
//                                                  @RequestParam(value = "gameId", required = false) String gameId) {
//        if (ValidStatus.VALID.name().equals(status)) {
//            Comment getComment = gameCommentService.findGameIdAndUid(Long.parseLong(gameId), Long.parseLong(uid));
//            if (getComment != null) {
//                return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, ResultCodeConstants.COMMENT_HAS_EXIST.getMsg(), ResultCodeConstants.COMMENT_HAS_EXIST.getMsg())).body(null);
//            }
//        }
//        boolean bool = gameCommentService.deleteCommentById(id, status);
//        return new ResponseEntity<>(bool, HttpStatus.OK);
//    }
//
//    /**
//     * 评论成功OR删除成功后回调接口  type=1 回复成功  type=0删除成功
//     *
//     * @param id
//     * @param type
//     * @return
//     */
//    @PutMapping("/comments/tools/increply/{id}")
//    public ResponseEntity<String> increaseReplyById(@PathVariable Long id,
//                                                    @RequestParam(value = "type", required = false) String type) {
//        Comment comment = gameCommentService.findOne(id);
//        if (comment == null) {
//            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, ResultCodeConstants.COMMENT_HAS_EXIST.getMsg(), ResultCodeConstants.COMMENT_HAS_EXIST.getMsg())).body("error");
//        }
//        int num = "1".equals(type) ? 1 : -1;
//        gameCommentService.increaseReplyNumById(id, num);
//        return new ResponseEntity<>("success", HttpStatus.OK);
//    }
//
//    @GetMapping("/comments/agreelist/{id}")
//    @ApiOperation("点赞用户信息")
//    @Timed
//    public ResponseEntity<ScoreRangeRows<ProfileDTO>> queryAgreeList(@PathVariable Long id,
//                                                                     @RequestParam(value = "psize", defaultValue = "15") Integer pageSize,
//                                                                     @RequestParam(name = "flag", defaultValue = "-1") Double flag) {
//        log.debug("REST request to queryAgreeList Comment : {}", id);
//        ScoreRange scoreRange = new ScoreRange(-1, flag, pageSize, ScoreSort.DESC);
//        ScoreRangeRows<ProfileDTO> scoreRangeRows = gameCommentService.queryAgreeListByCommentId(id, scoreRange);
//        scoreRangeRows.setTotalRows(commentRedisRepository.getAgreeCountByCommentId(id).intValue());
//        return new ResponseEntity<>(scoreRangeRows, HttpStatus.OK);
//    }
//
//    @PutMapping("/comments/detail/{id}")
//    @Timed
//    public ResponseEntity<String> Comment(@PathVariable Long id,
//                                          @RequestParam(value = "status", defaultValue = "0") String status) {
//        log.debug("REST request to deleteComment Comment : {}", id);
//        Long uid = EnjoySecurityUtils.getCurrentUid();
//        if (uid == null || uid == 0) {
//            throw new BusinessException(ameCommentFacade.getMessage("user.not.login",null));
//        }
//        String userName = EnjoySecurityUtils.getCurrentUserName();//登录用户名
//        if (!TOOLS_USER_NAME.equals(userName)) {
//            throw new BusinessException(ameCommentFacade.getMessage("user.not.login",null));
//        }
//        Comment comment = gameCommentService.findOne(id);
//        if (comment != null) {
//            comment.setHighQuality(Integer.parseInt(status));
//            gameCommentService.update(comment);
//        }
//        return new ResponseEntity<>(ResultCodeConstants.SUCCESS.getExtmsg(), HttpStatus.OK);
//    }
//
//
//    @GetMapping("/comments/ids")
//    public ResponseEntity<Map<Long, CommentDTO>> findCommentByIds(@RequestParam(value = "ids") Long[] ids) {
//        log.debug("REST request to findCommentByIds Comment : {}", ids);
//        Long currentId = EnjoySecurityUtils.getCurrentUid();
//        Map<Long, CommentDTO> returnMap = gameCommentService.findCommentByIds(new HashSet<>(Arrays.asList(ids)), currentId);
//        return ResponseEntity.ok(returnMap);
//    }


}
