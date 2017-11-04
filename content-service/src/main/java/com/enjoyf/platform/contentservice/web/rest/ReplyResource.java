package com.enjoyf.platform.contentservice.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.enjoyf.platform.contentservice.domain.reply.Reply;
import com.enjoyf.platform.contentservice.service.ReplyService;
import com.enjoyf.platform.contentservice.web.rest.util.HeaderUtil;
import com.enjoyf.platform.contentservice.web.rest.util.PaginationUtil;
import io.swagger.annotations.ApiParam;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Reply.
 */
@RestController
@RequestMapping("/api")
public class ReplyResource {

    private final Logger log = LoggerFactory.getLogger(ReplyResource.class);

    private static final String ENTITY_NAME = "reply";

    private final ReplyService replyService;

    public ReplyResource(ReplyService replyService) {
        this.replyService = replyService;
    }

    /**
     * POST  /replies : Create a new reply.
     *
     * @param reply the reply to create
     * @return the ResponseEntity with status 201 (Created) and with body the new reply, or with status 400 (Bad Request) if the reply has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/replies")
    @Timed
    public ResponseEntity<Reply> createReply(@Valid @RequestBody Reply reply) throws URISyntaxException {
        log.debug("REST request to save Reply : {}", reply);
        if (reply.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new reply cannot already have an ID")).body(null);
        }
        Reply result = replyService.save(reply);
        return ResponseEntity.created(new URI("/api/replies/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /replies : Updates an existing reply.
     *
     * @param reply the reply to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated reply,
     * or with status 400 (Bad Request) if the reply is not valid,
     * or with status 500 (Internal Server Error) if the reply couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/replies")
    @Timed
    public ResponseEntity<Reply> updateReply(@Valid @RequestBody Reply reply) throws URISyntaxException {
        log.debug("REST request to update Reply : {}", reply);
        if (reply.getId() == null) {
            return createReply(reply);
        }
        Reply result = replyService.save(reply);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, reply.getId().toString()))
            .body(result);
    }

    /**
     * GET  /replies : get all the replies.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of replies in body
     */
    @GetMapping("/replies")
    @Timed
    public ResponseEntity<List<Reply>> getAllReplies(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of Replies");
        Page<Reply> page = replyService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/replies");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /replies/:id : get the "id" reply.
     *
     * @param id the id of the reply to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the reply, or with status 404 (Not Found)
     */
    @GetMapping("/replies/{id}")
    @Timed
    public ResponseEntity<Reply> getReply(@PathVariable Long id) {
        log.debug("REST request to get Reply : {}", id);
        Reply reply = replyService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(reply));
    }

    /**
     * DELETE  /replies/:id : delete the "id" reply.
     *
     * @param id the id of the reply to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/replies/{id}")
    @Timed
    public ResponseEntity<Void> deleteReply(@PathVariable Long id) {
        log.debug("REST request to delete Reply : {}", id);
        replyService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
