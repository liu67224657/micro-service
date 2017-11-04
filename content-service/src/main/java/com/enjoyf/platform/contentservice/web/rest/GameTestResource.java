package com.enjoyf.platform.contentservice.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.enjoyf.platform.autoconfigure.web.error.BusinessException;
import com.enjoyf.platform.common.domain.enumeration.ValidStatus;
import com.enjoyf.platform.contentservice.domain.game.Game;
import com.enjoyf.platform.contentservice.domain.gamecomment.GameComment;
import com.enjoyf.platform.contentservice.service.GameCommentService;
import com.enjoyf.platform.contentservice.service.GameService;
import com.enjoyf.platform.contentservice.service.GameTagService;
import com.enjoyf.platform.contentservice.service.dto.game.GameInfoDTO;
import com.enjoyf.platform.contentservice.service.dto.game.GameInfoSimpleDTO;
import com.enjoyf.platform.contentservice.service.dto.search.GameSearchDTO;
import com.enjoyf.platform.contentservice.service.facade.GameCommentFacade;
import com.enjoyf.platform.contentservice.service.mapper.SearchMapper;
import com.enjoyf.platform.contentservice.service.profileservice.ProfileServiceFeignClient;
import com.enjoyf.platform.contentservice.service.search.GameSearchService;
import com.enjoyf.platform.contentservice.service.userservice.UserProfileFeignClient;
import com.enjoyf.platform.contentservice.web.rest.util.AskUtil;
import com.enjoyf.platform.contentservice.web.rest.util.HeaderUtil;
import com.enjoyf.platform.contentservice.web.rest.util.PaginationUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.gson.Gson;
import io.github.jhipster.web.util.ResponseUtil;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * Created by ericliu on 2017/8/16.
 */
@RestController
@RequestMapping("/api/game/test")
public class GameTestResource {

    private final Logger log = LoggerFactory.getLogger(GameResource.class);

    private static final String ENTITY_NAME = "game";

    private final GameService gameService;
    private final GameSearchService gameSearchService;

    public GameTestResource(GameService gameService, GameSearchService gameSearchService) {
        this.gameService = gameService;
        this.gameSearchService = gameSearchService;
    }

    @GetMapping("/search/careate/{id}")
    @Timed
    public ResponseEntity<Game> saveGame(@PathVariable Long id) {
        log.debug("REST request to getAppGame GameDTO : {}", id);
        Game game = gameService.findOne(id);

        gameSearchService.saveGame(game);

//        GameSearchDTO searchDTO = SearchMapper.MAPPER.game2SearchDTO(game);
//
//
//
//        System.out.println(searchDTO);
//
//
//        try {
//            System.out.println("==from gson:="+);
//        } catch (JsonProcessingException e) {
//            e.printStackTrace();
//        }

        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(game));
    }

    @GetMapping("/search/careate/")
    @Timed
    public ResponseEntity<Page> saveGames(@ApiParam Pageable pageable) {
        log.debug("REST request to getAppGame GameDTO : {}", pageable);
        Page<Game> gamePage = gameService.findAll(pageable, "", "");

        for (Game game : gamePage.getContent()) {
            if (game.getValidStatus().equals(ValidStatus.VALID)) {
                gameSearchService.saveGame(game);
            }
        }
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(gamePage));
    }

    @DeleteMapping("/search/")
    @Timed
    public ResponseEntity<String> deleteGame(@ApiParam Pageable pageable) {
        log.debug("REST request to getAppGame GameDTO : {}", pageable);
        Page<Game> gamePage = gameService.findAll(pageable, "", "");

        for (Game game : gamePage.getContent()) {
            if (game.getValidStatus().equals(ValidStatus.VALID)) {
                gameSearchService.deleteGame(game.getId());
            }
        }
        return ResponseEntity.ok("success");
    }


    /**
     * GET  /simplegames/:id : get the "id" commentRating.
     *
     * @param id the id of the commentRating to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the commentRating, or with status 404 (Not Found)
     */
    @GetMapping("/game/simple/{id}")
    @Timed
    public ResponseEntity<GameInfoSimpleDTO> getCommentRating(@PathVariable Long id) {
        log.debug("REST request to get CommentRating : {}", id);

        GameInfoSimpleDTO gameDTO = gameService.findSimpleGameRatingDTOById(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(gameDTO));
    }

}
