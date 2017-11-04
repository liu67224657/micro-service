package com.enjoyf.platform.contentservice.service.impl;

import com.enjoyf.platform.autoconfigure.security.EnjoySecurityUtils;
import com.enjoyf.platform.common.domain.enumeration.ValidStatus;
import com.enjoyf.platform.common.util.CollectionUtil;
import com.enjoyf.platform.contentservice.domain.enumeration.GameLine;
import com.enjoyf.platform.contentservice.domain.game.Game;
import com.enjoyf.platform.contentservice.domain.game.GameExtJson;
import com.enjoyf.platform.contentservice.domain.game.GameSum;
import com.enjoyf.platform.contentservice.domain.game.GameTag;
import com.enjoyf.platform.contentservice.domain.game.enumeration.GameOperStatus;
import com.enjoyf.platform.contentservice.domain.game.enumeration.GameType;
import com.enjoyf.platform.contentservice.repository.jpa.GameRepository;
import com.enjoyf.platform.contentservice.repository.redis.RedisGameHandleRepository;
import com.enjoyf.platform.contentservice.repository.redis.RedisGameRepository;
import com.enjoyf.platform.contentservice.repository.redis.RedisGameSumRepository;
import com.enjoyf.platform.contentservice.repository.redis.RedisGameCommentHandleRepository;
import com.enjoyf.platform.contentservice.service.GameService;
import com.enjoyf.platform.contentservice.service.GameTagService;
import com.enjoyf.platform.contentservice.service.dto.GameTagDTO;
import com.enjoyf.platform.contentservice.service.dto.game.*;
import com.enjoyf.platform.contentservice.service.mapper.GameMapper;
import com.enjoyf.platform.contentservice.service.mapper.GameTagMapper;
import com.enjoyf.platform.contentservice.service.search.GameSearchService;
import com.enjoyf.platform.contentservice.web.rest.util.GameSolrUtil;
import com.enjoyf.platform.page.ScoreRange;
import com.enjoyf.platform.page.ScoreRangeRows;
import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;

/**
 * Service Implementation for managing Game.
 */
@Service
public class GameServiceImpl implements GameService {

    private final Logger log = LoggerFactory.getLogger(GameServiceImpl.class);

    private final GameRepository gameRepository;


    private final RedisGameRepository redisGameRepository;


    private final RedisGameHandleRepository redisGameHandleRepository;


    private final GameTagService gameTagService;


    private final RedisGameCommentHandleRepository redisGameCommentHandleRepository;

    private final RedisGameSumRepository redisGameSumRepository;

//    private final GameSolrUtil gameSolrUtil;

    private final GameSearchService gameSearchService;

    public GameServiceImpl(GameRepository gameRepository, RedisGameRepository redisGameRepository, RedisGameHandleRepository redisGameHandleRepository, GameTagService gameTagService, RedisGameCommentHandleRepository redisGameCommentHandleRepository, RedisGameSumRepository redisGameSumRepository, GameSolrUtil gameSolrUtil, GameSearchService gameSearchService) {
        this.gameRepository = gameRepository;
        this.redisGameRepository = redisGameRepository;
        this.redisGameHandleRepository = redisGameHandleRepository;
        this.gameTagService = gameTagService;
        this.redisGameCommentHandleRepository = redisGameCommentHandleRepository;
        this.redisGameSumRepository = redisGameSumRepository;
//        this.gameSolrUtil = gameSolrUtil;
        this.gameSearchService = gameSearchService;
    }

    /**
     * Save a game.
     *
     * @param game the entity to save
     * @return the persisted entity
     */
    @Override
    public Game save(Game game) {
        log.debug("Request to save Game : {}", game);

        Game result = gameRepository.save(game);
        redisGameRepository.save(result);
        redisGameHandleRepository.addGame(game);
        gameSearchService.saveGame(game);

        return result;
    }

    @Override
    public Game update(Game game) {
        Game gameByDb = findOne(game.getId());
        if (gameByDb == null) {
            return null;
        }

        Game result = gameRepository.save(game);
        redisGameRepository.save(result);

        if (!Strings.isNullOrEmpty(gameByDb.getGameTag())) {
            Set<String> beforeTagList = new HashSet<>(Splitter.on(",").omitEmptyStrings().splitToList(gameByDb.getGameTag()));
            for (String gameTagid : beforeTagList) {
                redisGameHandleRepository.removeGameByGameTagID(result.getId(), gameTagid);
            }
        }

        if (!Strings.isNullOrEmpty(game.getGameTag())) {
            Set<String> afterTagList = new HashSet<>(Splitter.on(",").omitEmptyStrings().splitToList(game.getGameTag()));
            for (String gameTagid : afterTagList) {
                redisGameHandleRepository.addGameByGameTagID(result.getId(), gameTagid);
            }
        }
        redisGameHandleRepository.addGame(result);

        //solr
        try {
            gameSearchService.saveGame(game);
        } catch (Exception e) {
            log.error("gameSearchService saveGame error.e: ", e);
        }
        return game;
    }

    /**
     * Get all the games.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    public Page<Game> findAll(Pageable pageable, String id, String name) {
        log.debug("Request to get all Games");
        Page<Game> result;
        if (!StringUtils.isEmpty(id)) {
            result = gameRepository.findAllById(pageable, Long.valueOf(id));
        } else if (!StringUtils.isEmpty(name)) {
            result = gameRepository.findAllByNameLike(pageable, "%" + name + "%");
        } else {
            result = gameRepository.findAll(pageable);
        }

        if (result != null && result.hasContent()) {
            //todo 是否需要这么做
            redisGameRepository.save(result.getContent());
        }
        return result;
    }

    @Override
    public List<Game> findAll(ValidStatus validStatus) {
        return gameRepository.findAll();
    }

    /**
     * Get one game by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    public Game findOne(Long id) {
        log.debug("Request to get Game : {}", id);
        Game game = redisGameRepository.findOne(id);
        if (game == null) {
            game = gameRepository.findOne(id);
            if (game != null) {
                redisGameRepository.save(game);
            }
        }
        return game;
    }

    /**
     * Delete the  game by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Game : {}", id);
        Game game = findOne(id);
        game.setValidStatus(ValidStatus.UNVALID);
        gameRepository.save(game);
        redisGameRepository.delete(id);
        redisGameHandleRepository.removeGame(game);

        try {
            gameSearchService.deleteGame(id);
        } catch (Exception e) {
            log.error("delete search index error.e:",e);
        }
    }


    @Override
    public Map<Long, Game> findByGameids(Set<Long> gameIds) {
        log.debug("Request to findByGameids gameIds : {}", gameIds);
        Map<Long, Game> gameMap = new HashMap<>();
        if (CollectionUtils.isEmpty(gameIds)) {
            return gameMap;
        }
        gameIds.forEach(id -> {
            Game game = findOne(id);
            if (game != null) {
                gameMap.put(id, game);
            }
        });
        return gameMap;
    }

    @Override
    public Map<Long, GameInfoSimpleDTO> findByGameRatingDTOids(Set<Long> gameIds) {
        log.debug("Request to findByGameRatingDTOids gameIds : {}", gameIds);
        Map<Long, GameInfoSimpleDTO> gameMap = new HashMap<>();
        if (CollectionUtils.isEmpty(gameIds)) {
            return gameMap;
        }
        gameIds.forEach(id -> {
            Game game = findOne(id);
            if (game != null) {
                gameMap.put(id, toGameRatingSimpleDTO(toGameSimpleDTO(game)));
            }
        });
        return gameMap;
    }

    @Override
    public ScoreRangeRows<GameInfoSimpleDTO> findByGameLine(GameLine gameLine, String gameTagid, ScoreRange scoreRange) {
        log.debug("Request to findByGameLine gameLine : {}", gameLine, gameTagid, scoreRange);
        Set<Long> gameIdSet = redisGameHandleRepository.findAllByGameLine(gameLine, gameTagid, scoreRange);
        return buildScoreRangeRows(scoreRange, gameIdSet);
    }


    @Override
    public GameInfoSimpleDTO findSimpleGameRatingDTOById(Long id) {
        log.debug("Request to findSimpleGameRatingDTOById id : {}", id);
        GameSimpleDTO gameSimpleDTO = findSimpleGameDTOById(id);
        if (gameSimpleDTO == null) {
            return null;
        }
        return toGameRatingSimpleDTO(gameSimpleDTO);
    }

    @Override
    public GameSimpleDTO findSimpleGameDTOById(Long id) {

        Game game = findOne(id);
        //状态
        if (game == null || !ValidStatus.VALID.equals(game.getValidStatus())) {
            return null;
        }

        Long userId = EnjoySecurityUtils.getCurrentUid();
        String loginDomain = EnjoySecurityUtils.getCurrentLoginDomain();

        //处理用户阅读的标签
        if (!StringUtils.isEmpty(loginDomain) && !"client".equals(loginDomain)) {
            redisGameHandleRepository.setUsertaghistorySet(userId, game);
        }


        GameSimpleDTO gameSimpleDTO = toGameSimpleDTO(game);

        //是否点评过
        Set<Long> gameIdSet = new HashSet<>();
        gameIdSet.add(gameSimpleDTO.getId());
        Set<Long> comentedGameIdSet = redisGameCommentHandleRepository.checkGameIsComment(userId, gameIdSet);
        gameSimpleDTO.setHasComment(comentedGameIdSet.contains(gameSimpleDTO.getId()));

        return gameSimpleDTO;
    }

    @Override
    public GameInfoDTO findGameRatingDTOById(Long id) {
        GameDTO gameDTO = findGameDTOById(id);
        if (gameDTO == null) {
            return null;
        }
        return toGameRatingDTO(gameDTO);
    }


    public GameDTO findGameDTOById(Long id) {
        log.debug("Request to findSimpleGameRatingDTOById id : {}", id);
        Game game = findOne(id);
        //状态
        if (game == null || !ValidStatus.VALID.equals(game.getValidStatus())) {
            return null;
        }

        Long userId = EnjoySecurityUtils.getCurrentUid();
//        String loginDomain = EnjoySecurityUtils.getCurrentLoginDomain();

        //处理用户阅读的标签
//        if (!StringUtils.isEmpty(loginDomain) && !"client".equals(loginDomain)) {
//            redisGameHandleRepository.setUsertaghistorySet(userId, game);
//        }

        GameDTO gameDTO = toGameDTO(game);
        if (userId != null && userId > 0) {
            //是否点评过
            Set<Long> gameIdSet = new HashSet<>();
            gameIdSet.add(gameDTO.getId());
            Set<Long> comentedGameIdSet = redisGameCommentHandleRepository.checkGameIsComment(userId, gameIdSet);
            gameDTO.setHasComment(comentedGameIdSet.contains(gameDTO.getId()));
        }


        return gameDTO;
    }

    @Override
    public void createGamePlayme(Long id, Long userId) {
        log.debug("Request to createGamePlayme id : {}", id, userId);
        redisGameHandleRepository.setUserplaygameSet(userId, id);
    }


    @Override
    public ScoreRangeRows<GameInfoSimpleDTO> myGame(Long userId, ScoreRange scoreRange) {
        log.debug("Request to myGame userId : {}", userId, scoreRange);


        Set<Long> gameIdSet = redisGameHandleRepository.getUserplaygameSet(userId, scoreRange);


        ScoreRangeRows<GameInfoSimpleDTO> gameVM = buildScoreRangeRows(scoreRange, gameIdSet);

        List<GameInfoSimpleDTO> gameSimpleDTOList = gameVM.getRows();
        //是否点评过
        Set<Long> comentedGameIdSet = redisGameCommentHandleRepository.checkGameIsComment(userId, gameIdSet);
        gameSimpleDTOList.forEach(vm -> vm.getGame().setHasComment(
            comentedGameIdSet.contains(vm.getGame().getId())
        ));


        gameVM.setRows(gameSimpleDTOList);

        return gameVM;
    }

    private ScoreRangeRows<GameInfoSimpleDTO> buildScoreRangeRows(ScoreRange scoreRange, Set<Long> gameIdSet) {
        ScoreRangeRows<GameInfoSimpleDTO> result = new ScoreRangeRows<>();
        result.setRange(scoreRange);
        if (CollectionUtil.isEmpty(gameIdSet)) {
            return result;
        }
        List<GameInfoSimpleDTO> gameList = new ArrayList<>();
        gameIdSet.forEach(id -> {
            Game game = findOne(id);
            if (game != null) {
                gameList.add(toGameRatingSimpleDTO(toGameSimpleDTO(game)));
            }
        });
        result.setRows(gameList);
        return result;
    }


    @Override
    public void updateGame(Long id, Double score, Integer scoresum) {
        log.debug("Request to updateGame  not use: {}", id, score, scoresum);
//        Game game = findOne(id);
//        if (game != null) {
//            GameExtJson json = game.getExtJson();
//            json.setScore(score);
//            json.setScoreSum(scoresum);
//            save(game);
        //更新游戏库评分及评分人数
//            gameSolrUtil.updateGameDB(id, score, scoresum);
//        }
    }

    @Override
    public Page<GameInfoSimpleDTO> searchGame(String name,
                                              Set<String> gameTag,
                                              GameType gameType,
                                              GameOperStatus gameOperStatus,
                                              Boolean pc, Boolean andoird, Boolean ios, Pageable pageable) {
        log.debug("Request to searchGame : name:{},gameTag:{} gameType:{} gameOperStatus:{} pc:{} andoird:{} ios:{} pageable:{}", name, gameTag, gameType, gameOperStatus, pc, andoird, ios, pageable);

        Page<GameInfoSimpleDTO> pageRows = new PageImpl<>(new ArrayList<>(), pageable, 0);

        Page<Long> filedValuePage = gameSearchService.searchGame(name, gameTag, gameType, gameOperStatus, pc, andoird, ios, pageable);

        if (CollectionUtils.isEmpty(filedValuePage.getContent())) {
            return pageRows;
        }

        Set<Long> gameIdSet = new HashSet<>();
        for (Long gameId : filedValuePage.getContent()) {
            gameIdSet.add(gameId);
        }

        List<GameInfoSimpleDTO> simpleVMList = new ArrayList<>();
        gameIdSet.forEach(id -> {
            Game game = findOne(id);
            if (game != null) {
                simpleVMList.add(toGameRatingSimpleDTO(toGameSimpleDTO(game)));
            }
        });
        pageRows = new PageImpl<>(simpleVMList, pageable, filedValuePage.getTotalElements());
        return pageRows;
    }

    private GameSimpleDTO toGameSimpleDTO(Game game) {
        GameSimpleDTO gameSimpleDTO = new GameSimpleDTO();
        gameSimpleDTO.setId(game.getId());
        gameSimpleDTO.setName(game.getName());
        gameSimpleDTO.setAndroid(game.isAndroid());
        gameSimpleDTO.setIos(game.isIos());
        gameSimpleDTO.setPc(game.isPc());
        GameExtJson json = game.getExtJson();
        if (json != null) {
            gameSimpleDTO.setGameLogo(StringUtils.isEmpty(json.getGameLogo()) ? "" : json.getGameLogo());
            gameSimpleDTO.setRecommend(StringUtils.isEmpty(json.getRecommend()) ? "" : json.getRecommend());
            gameSimpleDTO.setRecommendAuth(StringUtils.isEmpty(json.getRecommendAuth()) ? "" : json.getRecommendAuth());
            gameSimpleDTO.setGameDeveloper(StringUtils.isEmpty(json.getGameDeveloper()) ? "" : json.getGameDeveloper());
        }

        gameSimpleDTO.setCreateTime(game.getCreateTime().toInstant().toEpochMilli());


        //处理游戏标签
        List<GameTagDTO> gameTagDTOS = new ArrayList<>();

        if (game.getGameTag() != null) {
            List<String> gameTagids = Splitter.on(",").omitEmptyStrings().splitToList(game.getGameTag());
            List<Long> gameTagidsLong = new ArrayList<>();
            gameTagids.forEach(s -> gameTagidsLong.add(Long.valueOf(s)));
            List<GameTag> gameTags = gameTagService.findByGameids(gameTagidsLong);
            gameTags.forEach(gametag -> gameTagDTOS.add(GameTagMapper.MAPPER.toGameTagDTO(gametag)));
        }
        gameSimpleDTO.setGameTag(gameTagDTOS);
        return gameSimpleDTO;
    }

    private GameDTO toGameDTO(Game game) {
        GameDTO gameDTO = GameMapper.MAPPER.simple2GameDTO(toGameSimpleDTO(game));
        GameExtJson json = game.getExtJson();
        gameDTO.setAliasName(game.getAliasName());
        gameDTO.setGameType(game.getGameType());
        gameDTO.setOperStatus(game.getOperStatus());

        if (json != null) {
            gameDTO.setGamePublisher(StringUtils.isEmpty(json.getGamePublisher()) ? "" : json.getGamePublisher());
            gameDTO.setGameDeveloper(StringUtils.isEmpty(json.getGameDeveloper()) ? "" : json.getGameDeveloper());
            gameDTO.setVideo(StringUtils.isEmpty(json.getVideo()) ? "" : json.getVideo());
            gameDTO.setPicList(Splitter.on(",").omitEmptyStrings().splitToList(json.getPic()));
            gameDTO.setPrice((StringUtils.isEmpty(json.getPrice()) ? "" : json.getPrice()));
            gameDTO.setLanguage(Splitter.on(",").omitEmptyStrings().splitToList(StringUtils.isEmpty(json.getLanguage()) ? "" : json.getLanguage()));
            gameDTO.setIosDownload(StringUtils.isEmpty(json.getIosDownload()) ? "" : json.getIosDownload());
            gameDTO.setAndroidDownload(StringUtils.isEmpty(json.getAndroidDownload()) ? "" : json.getAndroidDownload());
            gameDTO.setGameDesc(StringUtils.isEmpty(json.getGameDesc()) ? "" : json.getGameDesc());
            gameDTO.setPcDownload(StringUtils.isEmpty(json.getPcDownload()) ? "" : json.getPcDownload());
            gameDTO.setOnline(json.isOnline());
            gameDTO.setSize(StringUtils.isEmpty(json.getSize()) ? "" : json.getSize());
            gameDTO.setRecommendAuth(StringUtils.isEmpty(json.getRecommendAuth()) ? "" : json.getRecommendAuth());
            gameDTO.setRecommend(StringUtils.isEmpty(json.getRecommend()) ? "" : json.getRecommend());
        }
        return gameDTO;
    }

    private GameInfoDTO toGameRatingDTO(GameDTO gameDTO) {
        GameInfoDTO gameVM = new GameInfoDTO();
        gameVM.setGame(gameDTO);

        GameSum gameSum = redisGameSumRepository.findOne(gameDTO.getId());
        if (gameSum != null) {
            GameSumDTO gameGameSumDTO = GameMapper.MAPPER.entity2GameSumDTO(gameSum);
            gameVM.setSum(gameGameSumDTO);
        }
        return gameVM;
    }

    private GameInfoSimpleDTO toGameRatingSimpleDTO(GameSimpleDTO gameSimpleDTO) {
        GameInfoSimpleDTO gameVM = new GameInfoSimpleDTO();
        gameVM.setGame(gameSimpleDTO);

        GameSum gameSum = redisGameSumRepository.findOne(gameSimpleDTO.getId());
        if (gameSum != null) {
            GameSumDTO gameGameSumDTO = GameMapper.MAPPER.entity2GameSumDTO(gameSum);
            gameVM.setSum(gameGameSumDTO);
        }
        return gameVM;
    }

    @Override
    public void modifyGameRating(Long gameId, Integer rating, Integer ratingUserSum) {
        redisGameSumRepository.addRating(gameId, rating, ratingUserSum);
    }

    @Override
    public void modifyGameRecommend(Long gameId, Integer gameRecommendValue, Integer recommendUserSum) {
        redisGameSumRepository.addRecommendValue(gameId, gameRecommendValue, recommendUserSum);
    }
}
