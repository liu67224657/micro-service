package com.enjoyf.platform.contentservice.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.enjoyf.platform.autoconfigure.web.error.BusinessException;
import com.enjoyf.platform.common.domain.enumeration.ValidStatus;
import com.enjoyf.platform.contentservice.domain.game.Game;
import com.enjoyf.platform.contentservice.event.ContentEventProcess;
import com.enjoyf.platform.contentservice.service.GameService;
import com.enjoyf.platform.contentservice.service.dto.game.GameDTO;
import com.enjoyf.platform.contentservice.service.search.GameSearchService;
import com.enjoyf.platform.contentservice.web.rest.util.HeaderUtil;
import com.enjoyf.platform.contentservice.web.rest.util.PaginationUtil;
import com.enjoyf.platform.event.content.ContentIndexBuildEvent;
import io.github.jhipster.web.util.ResponseUtil;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Game.
 */
@RestController
@RequestMapping("/api/tools/games")
public class ToolsGameResource extends AbstractBaseResource {

    private final Logger log = LoggerFactory.getLogger(ToolsGameResource.class);

    private static final String ENTITY_NAME = "game";

    private final GameService gameService;
    private final ContentEventProcess contentEventProcess;

    public ToolsGameResource(GameService gameService, ContentEventProcess contentEventProcess) {
        this.gameService = gameService;
        this.contentEventProcess = contentEventProcess;
    }

    /**
     * POST  /games : Create a new game.
     *
     * @return the ResponseEntity with status 201 (Created) and with body the new game, or with status 400 (Bad Request) if the game has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     * @RequestBody Game game
     */
    @PostMapping
    @Timed
    public ResponseEntity<Game> createGame(@RequestBody Game game) throws URISyntaxException {
        log.debug("REST request to save Game : {}", game);

        if (game.getId() != null) {
            throw new BusinessException(getMessage("param.empty", null));
        }
        Game result = gameService.save(game);
        return ResponseEntity.created(new URI("/api/games/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /games : Updates an existing game.
     *
     * @param game the game to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated game,
     * or with status 400 (Bad Request) if the game is not valid,
     * or with status 500 (Internal Server Error) if the game couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping
    @Timed
    public ResponseEntity<Game> updateGame(@RequestBody Game game) throws URISyntaxException {
        log.debug("REST request to update Game : {}", game);
        if (game.getId() == null) {
            throw new BusinessException(getMessage("param.empty", null));
        }
        Game result = gameService.update(game);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, game.getId().toString()))
            .body(result);
    }

    /**
     * GET  /games : get all the games.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of games in body
     */
    @GetMapping
    @Timed
    public ResponseEntity<List<Game>> getAllGames(@ApiParam Pageable pageable,
                                                  @RequestParam(value = "id", defaultValue = "") String id,
                                                  @RequestParam(value = "name", defaultValue = "") String name) {
        log.debug("REST request to get a page of Games");
        Page<Game> page = gameService.findAll(pageable, id, name);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/games");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @Timed
    public ResponseEntity<GameDTO> getGame(@PathVariable Long id) {
        log.debug("REST request to getAppGame GameDTO : {}", id);
        GameDTO gameDTO = gameService.findGameDTOById(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(gameDTO));
    }


    /**
     * DELETE  /games/:id : delete the "id" game.
     *
     * @param id the id of the game to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/{id}")
    @Timed
    public ResponseEntity<Void> deleteGame(@PathVariable Long id) {
        log.debug("REST request to delete Game : {}", id);
        gameService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }


    @GetMapping("/search/indexbuild")
    @Timed
    public ResponseEntity<String> saveGames(@ApiParam Pageable pageable) {
        log.debug("REST request to getAppGame GameDTO : {}", pageable);
//        Page<Game> gamePage = gameService.findAll(pageable, "", "");
//
//        for (Game game : gamePage.getContent()) {
//            if (game.getValidStatus().equals(ValidStatus.VALID)) {
//                gameSearchService.saveGame(game);
//            }
//        }
        contentEventProcess.send(new ContentIndexBuildEvent());
        return ResponseEntity.ok("success");
    }


}
