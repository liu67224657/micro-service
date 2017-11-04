package com.enjoyf.platform.contentservice.repository.jpa;

import com.enjoyf.platform.common.domain.enumeration.ValidStatus;
import com.enjoyf.platform.contentservice.domain.game.Game;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Game entity.
 */
@SuppressWarnings("unused")
public interface GameRepository extends JpaRepository<Game, Long> {

    Page<Game> findAllById(Pageable pageable, Long id);

    Page<Game> findAllByNameLike(Pageable pageable, String name);

    List<Game> findAllByValidStatus(ValidStatus validStatus);

    @Modifying
    @Query("update  Game l set l.validStatus = ?2 where l.id = ?1 ")
    int setValidStatusById(Long id, ValidStatus validStatus);
}
