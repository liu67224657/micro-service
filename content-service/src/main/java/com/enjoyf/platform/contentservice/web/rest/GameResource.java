package com.enjoyf.platform.contentservice.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.enjoyf.platform.autoconfigure.context.CommonContextHolder;
import com.enjoyf.platform.autoconfigure.security.EnjoySecurityUtils;
import com.enjoyf.platform.autoconfigure.web.CommonParams;
import com.enjoyf.platform.autoconfigure.web.error.BusinessException;
import com.enjoyf.platform.common.util.CollectionUtil;
import com.enjoyf.platform.contentservice.domain.enumeration.GameLine;
import com.enjoyf.platform.contentservice.domain.game.Game;
import com.enjoyf.platform.contentservice.domain.game.GameExtJson;
import com.enjoyf.platform.contentservice.domain.game.enumeration.GameOperStatus;
import com.enjoyf.platform.contentservice.domain.game.enumeration.GameType;
import com.enjoyf.platform.contentservice.service.dto.game.GameDTO;
import com.enjoyf.platform.contentservice.service.profileservice.ProfileServiceFeignClient;
import com.enjoyf.platform.contentservice.service.profileservice.domain.WikiAppProfileDTO;
import com.enjoyf.platform.contentservice.service.GameService;
import com.enjoyf.platform.contentservice.service.GameTagService;
import com.enjoyf.platform.contentservice.service.dto.game.GameInfoDTO;
import com.enjoyf.platform.contentservice.service.dto.game.GameInfoSimpleDTO;
import com.enjoyf.platform.contentservice.service.dto.game.GameSimpleDTO;
import com.enjoyf.platform.contentservice.web.rest.util.AskUtil;
import com.enjoyf.platform.contentservice.web.rest.util.HeaderUtil;
import com.enjoyf.platform.contentservice.web.rest.util.PaginationUtil;
import com.enjoyf.platform.contentservice.web.rest.vm.GameIndexDataVM;
import com.enjoyf.platform.contentservice.web.rest.vm.GameIndexVM;
import com.enjoyf.platform.contentservice.web.rest.vm.GameTagVM;
import com.enjoyf.platform.page.ScoreRange;
import com.enjoyf.platform.page.ScoreRangeRows;
import com.enjoyf.platform.page.ScoreSort;
import com.google.common.base.Strings;
import io.github.jhipster.web.util.ResponseUtil;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * REST controller for managing Game.
 */
@RestController
@RequestMapping("/api/games")
public class GameResource extends AbstractBaseResource {

    private final Logger log = LoggerFactory.getLogger(GameResource.class);

    private static final String ENTITY_NAME = "game";

    private final GameService gameService;

    private final GameTagService gameTagService;

    private final ProfileServiceFeignClient profileServiceFeignClient;

    @Autowired
    private AskUtil askUtil;

    public GameResource(GameService gameService, GameTagService gameTagService, ProfileServiceFeignClient profileServiceFeignClient) {
        this.gameService = gameService;
        this.gameTagService = gameTagService;
        this.profileServiceFeignClient = profileServiceFeignClient;
    }


    @GetMapping("/{id}")
    @Timed
    public ResponseEntity<GameDTO> getGame(@PathVariable Long id) {
        log.debug("REST request to getAppGame GameDTO : {}", id);
        GameDTO gameDTO = gameService.findGameDTOById(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(gameDTO));
    }

    @GetMapping("/info/{id}")
    @Timed
    public ResponseEntity<GameInfoDTO> getGameInfo(@PathVariable Long id) {
        log.debug("REST request to getAppGame GameDTO : {}", id);
        GameInfoDTO gameDTO = gameService.findGameRatingDTOById(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(gameDTO));
    }


    /**
     * GET  /simplegames/:id : get the "id" commentRating.
     *
     * @param id the id of the commentRating to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the commentRating, or with status 404 (Not Found)
     */
    @GetMapping("/info/simple/{id}")
    @Timed
    public ResponseEntity<GameInfoSimpleDTO> getSimpleInfo(@PathVariable Long id) {
        log.debug("REST request to get CommentRating : {}", id);

        GameInfoSimpleDTO gameDTO = gameService.findSimpleGameRatingDTOById(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(gameDTO));
    }

    @GetMapping("/line/{gametagid}/{gameline}")
    @Timed
    @ApiOperation(value = "app获取游戏标签列表", response = GameSimpleDTO.class)
    public ResponseEntity<ScoreRangeRows<GameInfoSimpleDTO>> getGameLines(
        @PathVariable String gametagid, @PathVariable int gameline,
        @RequestParam(value = "psize", defaultValue = "15") Integer pageSize,
        @RequestParam(name = "flag", defaultValue = "-1") Double flag) {
        log.debug("REST request to get getGameLines : {}", gameline, flag);
        ScoreRange scoreRange = new ScoreRange(-1, flag, pageSize, ScoreSort.DESC);
        GameLine gameLine = null;
        if (gameline == 0) {
            gameLine = GameLine.NEW_GAME_LIEN;
        } else if (gameline == 1) {
            gameLine = GameLine.HOT_GAME_LIEN;
        }
        ScoreRangeRows<GameInfoSimpleDTO> gameDTOScoreRangeRows = gameService.findByGameLine(gameLine, gametagid, scoreRange);
        return ResponseEntity.ok(gameDTOScoreRangeRows);
    }

    @GetMapping("/ids")
    @Timed
    @ApiOperation(value = "根据游戏id集合查询游戏", response = Boolean.class)
    public ResponseEntity<Map<Long, Game>> getByGameIds(@RequestParam(value = "ids") Long[] ids) {
        log.debug("REST request to get ids getByGameIds: {}", ids);
        Map<Long, Game> gameMap = gameService.findByGameids(Arrays.stream(ids).collect(Collectors.toSet()));
        return ResponseEntity.ok(gameMap);
    }


    @PostMapping("/playme/{id}")
    @Timed
    @ApiOperation(value = "app玩玩看", response = Boolean.class)
    public ResponseEntity<Boolean> createGamePlayme(@PathVariable Long id) throws URISyntaxException {
        log.debug("REST request to createGamePlayme Game : {}", id);

        Long userId = EnjoySecurityUtils.getCurrentUid();
        if (userId == null || userId <= 0) {
            throw new BusinessException("获取用户失败", "userId:" + userId);
        }
        gameService.createGamePlayme(id, userId);
        return ResponseEntity.ok(true);
    }

    @GetMapping("/mygame")
    @Timed
    @ApiOperation(value = "app我的游戏", response = GameSimpleDTO.class)
    public ResponseEntity<ScoreRangeRows<GameInfoSimpleDTO>> mygame(@RequestParam(value = "psize", defaultValue = "16") Integer pageSize,
                                                                    @RequestParam(name = "flag", defaultValue = "-1") Double flag) throws URISyntaxException {
        log.debug("REST request to mygame Game : {}", flag);
        Long userId = EnjoySecurityUtils.getCurrentUid();
        if (userId == null || userId <= 0) {
            throw new BusinessException("获取用户失败", "userId:" + userId);
        }
        ScoreRange scoreRange = new ScoreRange(-1, flag, pageSize, ScoreSort.DESC);
        ScoreRangeRows<GameInfoSimpleDTO> gameDTOScoreRangeRows = gameService.myGame(userId, scoreRange);
        return ResponseEntity.ok(gameDTOScoreRangeRows);
    }


    @GetMapping("/search")
    @Timed
    @ApiOperation(value = "点评游戏搜索，gameTag为标签ID，多个参数时候用\",\"分割", response = GameSimpleDTO.class)
    public ResponseEntity<List<GameInfoSimpleDTO>> search(@ApiParam Pageable pageable,
                                                          @RequestParam(required = false) String name,
                                                          @RequestParam(required = false) String gameTag,
                                                          @RequestParam(required = false) GameType gameType,
                                                          @RequestParam(required = false) GameOperStatus gameOperStatus,
                                                          @RequestParam(required = false) Boolean pc,
                                                          @RequestParam(required = false) Boolean andoird,
                                                          @RequestParam(required = false) Boolean ios
    ) throws URISyntaxException {
        log.debug("REST request to search Game : name:{},gameTag:{} gameType:{} gameOperStatus:{} pc:{} andoird:{} ios:{} pageable:{}", name, gameTag, gameType, gameOperStatus, pc, andoird, ios, pageable);

        Set<String> gameTags = new HashSet<>();
        if (!Strings.isNullOrEmpty(gameTag)) {
            for (String gameTagString : gameTag.split(",")) {
                gameTags.add(gameTagString);
            }
        }

        Page<GameInfoSimpleDTO> page = gameService.searchGame(name, gameTags, gameType, gameOperStatus, pc, andoird, ios, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/games/search/");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }


    @GetMapping("/line/index")
    @Timed
    @ApiOperation(value = "首页推荐游戏", response = GameIndexDataVM.class)
    public ResponseEntity<GameIndexDataVM> getGameLinesIndex(@RequestParam(value = "gamePsize", defaultValue = "16") Integer gamePsize,
                                                             @RequestParam(name = "gameFlag", defaultValue = "-1") Double gameFlag,
                                                             @RequestParam(name = "profileFlag", defaultValue = "-1") Double profileFlag,
                                                             @RequestParam(value = "profilePsize", defaultValue = "4") Integer profilePsize,
                                                             @RequestHeader HttpHeaders headers) {
        log.debug("REST request to get getGameLinesIndex : {}", gamePsize, gameFlag, profilePsize, profileFlag);
        //游戏
        ScoreRange scoreRange = new ScoreRange(-1, gameFlag, gamePsize, ScoreSort.DESC);
        ScoreRangeRows<GameInfoSimpleDTO> gameDTOScoreRangeRows = gameService.findByGameLine(GameLine.GAME_INDEX_SET, "", scoreRange);

        //推荐用户
        ScoreRangeRows<WikiAppProfileDTO> profileRange = new ScoreRangeRows<>();
        try {
            profileRange = profileServiceFeignClient.recommend(profilePsize, profileFlag);
        } catch (Exception e) {
            log.error("/line/index getGameLinesIndex recommend", e);
        }

        //返回数据
        GameIndexDataVM gameIndexDataVM = new GameIndexDataVM();
        List<GameIndexVM> rows = new ArrayList<>();


        //处理第一页
        if (gameFlag == -1) {
            processFirstPage(gameDTOScoreRangeRows, profileRange, rows, gameIndexDataVM);
        }

        if (gameFlag != -1) {
            processNextPage(gameDTOScoreRangeRows, profileRange, rows);
        }

        gameIndexDataVM.setRows(rows);
        gameIndexDataVM.setGameScoreRange(gameDTOScoreRangeRows.getRange());
        gameIndexDataVM.setProfileScoreRange(gameDTOScoreRangeRows.getRange());
        return ResponseEntity.ok(gameIndexDataVM);
    }

    private void processFirstPage(ScoreRangeRows<GameInfoSimpleDTO> gameDTOScoreRangeRows, ScoreRangeRows<WikiAppProfileDTO> profileRange,
                                  List<GameIndexVM> rows, GameIndexDataVM gameIndexDataVM) {
        int tempIndex = 0;
        List<WikiAppProfileDTO> wikiAppProfileDTOS = profileRange.getRows();
        List<WikiAppProfileDTO> tempProfie = new ArrayList<>();
        tempProfie.addAll(wikiAppProfileDTOS);

        //gameDTOScoreRangeRows.setRows(gameDTOScoreRangeRows.getRows().subList(0, 4));

        for (int i = 0; i < gameDTOScoreRangeRows.getRows().size(); i++) {
            if (i >= 8 && i % 4 == 0) {
                int start = tempIndex * 2;
                int end = tempIndex * 2 + 2;
                if (wikiAppProfileDTOS.size() > start) {
                    wikiAppProfileDTOS.subList(start, wikiAppProfileDTOS.size() > end ? end : wikiAppProfileDTOS.size()).forEach(dto -> {
                        rows.add(new GameIndexVM(3, dto));
                        tempProfie.remove(dto);
                    });
                }
                tempIndex++;
            }
            rows.add(new GameIndexVM(1, gameDTOScoreRangeRows.getRows().get(i)));
            if (i == 3) {
                Long uid = EnjoySecurityUtils.getCurrentUid();
                List<GameTagVM> list = gameTagService.findGameTagByuid(uid);
                rows.add(new GameIndexVM(2, list));
            }
        }


        //处理游戏不足的情况
        if (!CollectionUtil.isEmpty(tempProfie)) {
            //先放标签
            if (gameDTOScoreRangeRows.getRows().size() < 4) {
                Long uid = EnjoySecurityUtils.getCurrentUid();
                List<GameTagVM> list = gameTagService.findGameTagByuid(uid);
                rows.add(new GameIndexVM(2, list));
            }
            tempProfie.forEach(dto -> {
                rows.add(new GameIndexVM(3, dto));
            });
        }


        //轮播图
        CommonParams commonParams = CommonContextHolder.getContext().getCommonParams();
        if (commonParams != null) {
            gameIndexDataVM.setHeadItems(askUtil.getSlideshow(commonParams.getPlatform()));
        }
    }

    private void processNextPage(ScoreRangeRows<GameInfoSimpleDTO> gameDTOScoreRangeRows, ScoreRangeRows<WikiAppProfileDTO> profileRange, List<GameIndexVM> rows) {
        int tempIndex = 0;
        List<WikiAppProfileDTO> wikiAppProfileDTOS = profileRange.getRows();
        List<WikiAppProfileDTO> tempProfie = new ArrayList<>();
        tempProfie.addAll(wikiAppProfileDTOS);
        for (int i = 0; i < gameDTOScoreRangeRows.getRows().size(); i++) {
            if (i % 4 == 0) {
                int start = tempIndex * 2;
                int end = tempIndex * 2 + 2;
                if (wikiAppProfileDTOS.size() > start) {
                    wikiAppProfileDTOS.subList(start, wikiAppProfileDTOS.size() > end ? end : wikiAppProfileDTOS.size()).forEach(dto -> {
                        rows.add(new GameIndexVM(3, dto));
                        tempProfie.remove(dto);
                    });
                }
                tempIndex++;
            }
            rows.add(new GameIndexVM(1, gameDTOScoreRangeRows.getRows().get(i)));
        }


        if (!CollectionUtil.isEmpty(tempProfie)) {
            tempProfie.forEach(dto -> {
                rows.add(new GameIndexVM(3, dto));
            });
        }
    }

}
