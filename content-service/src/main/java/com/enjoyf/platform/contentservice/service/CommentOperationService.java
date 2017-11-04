package com.enjoyf.platform.contentservice.service;

import com.enjoyf.platform.contentservice.domain.gamecomment.GameCommentOperation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing CommentOperation.
 */
public interface CommentOperationService {

    /**
     * Save a commentOperation.
     *
     * @param gameCommentOperation the entity to save
     * @return the persisted entity
     */
    GameCommentOperation save(GameCommentOperation gameCommentOperation);

    /**
     *  Get all the commentOperations.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<GameCommentOperation> findAll(Pageable pageable);

    /**
     *  Get the "id" commentOperation.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    GameCommentOperation findOne(Long id);

    /**
     *  Delete the "id" commentOperation.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);
}
