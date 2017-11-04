package com.enjoyf.platform.contentservice.service.impl;

import com.enjoyf.platform.contentservice.service.CommentOperationService;
import com.enjoyf.platform.contentservice.domain.gamecomment.GameCommentOperation;
import com.enjoyf.platform.contentservice.repository.jpa.GameCommentOperationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

/**
 * Service Implementation for managing CommentOperation.
 */
@Service
@Transactional
public class CommentOperationServiceImpl implements CommentOperationService{

    private final Logger log = LoggerFactory.getLogger(CommentOperationServiceImpl.class);

    private final GameCommentOperationRepository gameCommentOperationRepository;

    public CommentOperationServiceImpl(GameCommentOperationRepository gameCommentOperationRepository) {
        this.gameCommentOperationRepository = gameCommentOperationRepository;
    }

    /**
     * Save a commentOperation.
     *
     * @param gameCommentOperation the entity to save
     * @return the persisted entity
     */
    @Override
    public GameCommentOperation save(GameCommentOperation gameCommentOperation) {
        log.debug("Request to save CommentOperation : {}", gameCommentOperation);
        GameCommentOperation result = gameCommentOperationRepository.save(gameCommentOperation);
        return result;
    }

    /**
     *  Get all the commentOperations.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<GameCommentOperation> findAll(Pageable pageable) {
        log.debug("Request to get all CommentOperations");
        Page<GameCommentOperation> result = gameCommentOperationRepository.findAll(pageable);
        return result;
    }

    /**
     *  Get one commentOperation by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public GameCommentOperation findOne(Long id) {
        log.debug("Request to get CommentOperation : {}", id);
        GameCommentOperation gameCommentOperation = gameCommentOperationRepository.findOne(id);
        return gameCommentOperation;
    }

    /**
     *  Delete the  commentOperation by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete CommentOperation : {}", id);
        gameCommentOperationRepository.delete(id);
    }
}
