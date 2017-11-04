package com.enjoyf.platform.contentservice.repository.jpa;

import com.enjoyf.platform.common.domain.enumeration.ValidStatus;
import com.enjoyf.platform.contentservice.domain.gamecomment.GameComment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 * Spring Data JPA repository for the Comment entity.
 */
@SuppressWarnings("unused")
public interface GameCommentRepository extends JpaRepository<GameComment, Long>, JpaSpecificationExecutor {


    Page<GameComment> findAllByGameIdAndValidStatus(long gameId, ValidStatus validStatus, Pageable pageable);

    GameComment findOneByUidAndGameId(long uid, long gameId);

    List<GameComment> findAllByIdIn(List<Long> ids);

}
