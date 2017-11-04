package com.enjoyf.platform.contentservice.service;

import com.enjoyf.platform.common.domain.enumeration.ValidStatus;
import com.enjoyf.platform.contentservice.domain.enumeration.GameLine;
import com.enjoyf.platform.contentservice.domain.game.Game;
import com.enjoyf.platform.contentservice.domain.game.enumeration.GameOperStatus;
import com.enjoyf.platform.contentservice.domain.game.enumeration.GameType;
import com.enjoyf.platform.contentservice.service.dto.game.GameDTO;
import com.enjoyf.platform.contentservice.service.dto.game.GameInfoDTO;
import com.enjoyf.platform.contentservice.service.dto.game.GameInfoSimpleDTO;
import com.enjoyf.platform.contentservice.service.dto.game.GameSimpleDTO;
import com.enjoyf.platform.page.ScoreRange;
import com.enjoyf.platform.page.ScoreRangeRows;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Service Interface for managing Game.
 */
public interface GameService {

    /**
     * Save a game.
     *
     * @param game the entity to save
     * @return the persisted entity
     */
    Game save(Game game);

    Game update(Game game);

    /**
     * Get all the games.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<Game> findAll(Pageable pageable, String id, String name);


    List<Game> findAll(ValidStatus validStatus);

    /**
     * Get the "id" game.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Game findOne(Long id);

    /**
     * Delete the "id" game.
     *
     * @param id the id of the entity
     */
    void delete(Long id);


    /**
     * 根据游戏id集合查找游戏
     *
     * @param gameIds
     * @return
     */
    Map<Long, Game> findByGameids(Set<Long> gameIds);


    /**
     * 根据游戏id集合查找游戏
     *
     * @param gameIds
     * @return
     */
    Map<Long, GameInfoSimpleDTO> findByGameRatingDTOids(Set<Long> gameIds);


    /**
     * @param gameLine
     * @param scoreRange
     * @return
     */
    ScoreRangeRows<GameInfoSimpleDTO> findByGameLine(GameLine gameLine, String gameTagid, ScoreRange scoreRange);


    /**
     * get游戏+评分的简单游戏信息
     *
     * @param id
     * @return
     */
    GameInfoDTO findGameRatingDTOById(Long id);

    /**
     * get游戏信息
     *
     * @param id
     * @return
     */
    GameSimpleDTO findSimpleGameDTOById(Long id);


    /**
     * 包含简介等信息内容较多
     *
     * @param id
     * @return
     */
    GameInfoSimpleDTO findSimpleGameRatingDTOById(Long id);

    GameDTO findGameDTOById(Long id);


    void createGamePlayme(Long id, Long userId);


    ScoreRangeRows<GameInfoSimpleDTO> myGame(Long userId, ScoreRange scoreRange);


    /**
     * todo
     *
     * @param id       游戏ID
     * @param score    评分
     * @param scoresum
     */
    @Deprecated
    void updateGame(Long id, Double score, Integer scoresum);


    Page<GameInfoSimpleDTO> searchGame(String name,
                                       Set<String> gameTag,
                                       GameType gameType,
                                       GameOperStatus gameOperStatus,
                                       Boolean pc, Boolean android, Boolean ios, Pageable pageable);


    void modifyGameRating(Long gameId, Integer rating, Integer ratingUserSum);

    void modifyGameRecommend(Long gameId, Integer gameRecommendValue, Integer recommendUserSum);
}
