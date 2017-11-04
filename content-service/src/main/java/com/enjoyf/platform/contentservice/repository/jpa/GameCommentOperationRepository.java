package com.enjoyf.platform.contentservice.repository.jpa;

import com.enjoyf.platform.contentservice.domain.gamecomment.GameCommentOperation;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the CommentOperation entity.
 */
@SuppressWarnings("unused")
public interface GameCommentOperationRepository extends JpaRepository<GameCommentOperation,Long> {

    List<GameCommentOperation> findByCommentIdAndUid(Long commentId, Long uid);

}
