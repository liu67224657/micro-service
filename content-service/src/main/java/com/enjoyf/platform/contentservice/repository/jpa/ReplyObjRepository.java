package com.enjoyf.platform.contentservice.repository.jpa;

import com.enjoyf.platform.contentservice.domain.reply.ReplyObj;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the ReplyObj entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ReplyObjRepository extends JpaRepository<ReplyObj, Long> {

}
