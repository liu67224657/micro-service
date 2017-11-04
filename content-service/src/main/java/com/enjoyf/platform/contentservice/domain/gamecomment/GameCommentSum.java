package com.enjoyf.platform.contentservice.domain.gamecomment;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.redis.core.RedisHash;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Comment sum.
 */
public class GameCommentSum implements Serializable {
    private Long commentId;

    private Integer replySum;
    private Integer agreeSum;

    public Long getCommentId() {
        return commentId;
    }

    public void setCommentId(Long commentId) {
        this.commentId = commentId;
    }

    public Integer getReplySum() {
        return replySum;
    }

    public void setReplySum(Integer replySum) {
        this.replySum = replySum;
    }

    public Integer getAgreeSum() {
        return agreeSum;
    }

    public void setAgreeSum(Integer agreeSum) {
        this.agreeSum = agreeSum;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        GameCommentSum sum = (GameCommentSum) o;
        if (sum.getCommentId() == null || getCommentId() == null) {
            return false;
        }
        return Objects.equals(getCommentId(), sum.getCommentId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getCommentId());
    }


    @Override
    public String toString() {
        return "GameCommentSum{" +
            "commentId=" + commentId +
            ", replySum=" + replySum +
            ", agreeSum=" + agreeSum +
            '}';
    }
}
