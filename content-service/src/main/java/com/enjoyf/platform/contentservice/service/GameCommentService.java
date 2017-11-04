package com.enjoyf.platform.contentservice.service;

import com.enjoyf.platform.contentservice.domain.gamecomment.GameComment;
import com.enjoyf.platform.contentservice.domain.gamecomment.GameCommentOperation;
import com.enjoyf.platform.contentservice.service.dto.gamecomment.GameCommentInfoDTO;
import com.enjoyf.platform.page.ScoreRange;
import com.enjoyf.platform.page.ScoreRangeRows;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing CommentRating.
 */
public interface GameCommentService {

    /**
     * Save a commentRating.
     *
     * @param gameComment the entity to save
     * @return the persisted entity
     */
    GameComment save(GameComment gameComment);

    GameComment update(GameComment gameComment);

    GameComment findByUIdAndGameId(Long uid, Long gameId);

    /**
     * Get all the comments.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<GameComment> findAll(Pageable pageable);

    /**
     * Get the "id" comment.
     *
     * @param id the id of the entity
     * @return the entity
     */
    GameComment findOne(Long id);

    /**
     * Delete the "gameComment"
     *
     * @param gameComment the id of the entity
     */
    boolean delete(GameComment gameComment);

    /**
     *  agree
     *
     * @param operation obj
     * @return true-success false-faild
     */
    boolean agree(GameCommentOperation operation);

    /**
     * remove uid
     *
     * @param commentId obj
     * @return true-success false-faild
     */
    boolean delAgree(Long uid, Long commentId);

    ScoreRangeRows<GameCommentInfoDTO> findByGameId(Long gameId, Long uid, ScoreRange scoreRange, CommentIdQueryType type);
}
