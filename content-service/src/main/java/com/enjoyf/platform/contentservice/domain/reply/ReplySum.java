package com.enjoyf.platform.contentservice.domain.reply;

import com.enjoyf.platform.common.domain.enumeration.ValidStatus;
import com.enjoyf.platform.contentservice.domain.enumeration.ReplyType;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.redis.core.RedisHash;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A Reply.
 */
public class ReplySum implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private Integer replySum;
    private Integer maxFloorNum;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getReplySum() {
        return replySum;
    }

    public void setReplySum(Integer replySum) {
        this.replySum = replySum;
    }

    public Integer getMaxFloorNum() {
        return maxFloorNum;
    }

    public void setMaxFloorNum(Integer maxFloorNum) {
        this.maxFloorNum = maxFloorNum;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ReplySum reply = (ReplySum) o;
        if (reply.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), reply.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ReplySum{" +
            "id=" + id +
            ", replySum=" + replySum +
            ", maxFloorNum=" + maxFloorNum +
            '}';
    }
}
