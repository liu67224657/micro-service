package com.enjoyf.platform.contentservice.service;

import com.enjoyf.platform.contentservice.domain.reply.Reply;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing Reply.
 */
public interface ReplyService {

    /**
     * Save a reply.
     *
     * @param reply the entity to save
     * @return the persisted entity
     */
    Reply save(Reply reply);

    /**
     * Get all the replies.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<Reply> findAll(Pageable pageable);

    /**
     * Get the "id" reply.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Reply findOne(Long id);

    /**
     * Delete the "id" reply.
     *
     * @param id the id of the entity
     */
    boolean delete(Long id);
}
