package com.enjoyf.platform.contentservice.domain.reply;

import com.enjoyf.platform.contentservice.domain.enumeration.ReplyType;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A ReplyObj.
 */
@Entity
@Table(name = "reply_obj")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ReplyObj implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(min = 32, max = 32)
    @Column(name = "reply_obj_id", length = 32, nullable = false)
    private String replyObjId;

    @NotNull
    @Column(name = "dest_id", nullable = false)
    private String destId;

    @NotNull
    @Column(name = "reply_type", nullable = false)
    @Enumerated
    private ReplyType replyType;

    @Column(name = "agree_sum")
    private Integer agreeSum;

    @Column(name = "max_floor_Num")
    private Integer maxFloorNum;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getReplyObjId() {
        return replyObjId;
    }

    public void setReplyObjId(String replyObjId) {
        this.replyObjId = replyObjId;
    }

    public String getDestId() {
        return destId;
    }

    public void setDestId(String destId) {
        this.destId = destId;
    }

    public ReplyType getReplyType() {
        return replyType;
    }

    public void setReplyType(ReplyType replyType) {
        this.replyType = replyType;
    }

    public Integer getAgreeSum() {
        return agreeSum;
    }

    public void setAgreeSum(Integer agreeSum) {
        this.agreeSum = agreeSum;
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
        ReplyObj replyObj = (ReplyObj) o;
        if (replyObj.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), replyObj.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ReplyObj{" +
            "id=" + getId() +
            ", replyUniqueId='" + getReplyObjId() + "'" +
            ", destId='" + getDestId() + "'" +
            ", replyType='" + getReplyType() + "'" +
            ", agreeSum='" + getAgreeSum() + "'" +
            "}";
    }
}
