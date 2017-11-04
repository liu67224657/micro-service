package com.enjoyf.platform.contentservice.domain.reply;

import com.enjoyf.platform.common.domain.enumeration.ValidStatus;
import com.enjoyf.platform.contentservice.domain.enumeration.ReplyType;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.annotation.*;
import org.springframework.data.redis.core.RedisHash;

import javax.persistence.*;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A Reply.
 */
@Entity
@Table(name = "reply")
@RedisHash("reply")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Reply implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @org.springframework.data.annotation.Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "uid", nullable = false)
    private Long uid;

    @Column(name = "parent_id")
    private Long parentId = 0l;

    @Column(name = "parent_uid")
    private Long parentUid = 0l;

    @Column(name = "root_id")
    private Long rootId = 0l;

    @Column(name = "root_uid")
    private Long rootUid = 0l;

    @Column(name = "body")
    private String body;

    @Column(name = "pic")
    private String pic;

    @Column(name = "create_time")
    private ZonedDateTime createTime;

    @NotNull
    @Enumerated
    @Column(name = "valid_status", nullable = false)
    private ValidStatus validStatus = ValidStatus.VALID;

    @NotNull
    @Column(name = "reply_type", nullable = false)
    @Enumerated
    private ReplyType replyType;

    @NotNull
    @Column(name = "dest_id", nullable = false)
    private String destId;

    @NotNull
    @Size(min = 32, max = 32)
    @Column(name = "reply_obj_id", length = 32, nullable = false)
    private String replyObjId;

    @NotNull
    @Column(name = "floor_num", nullable = false)
    private long floorNum=0;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public Long getParentUid() {
        return parentUid;
    }

    public void setParentUid(Long parentUid) {
        this.parentUid = parentUid;
    }

    public Long getRootId() {
        return rootId;
    }

    public void setRootId(Long rootId) {
        this.rootId = rootId;
    }

    public Long getRootUid() {
        return rootUid;
    }

    public void setRootUid(Long rootUid) {
        this.rootUid = rootUid;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public ZonedDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(ZonedDateTime createTime) {
        this.createTime = createTime;
    }

    public ValidStatus getValidStatus() {
        return validStatus;
    }

    public void setValidStatus(ValidStatus validStatus) {
        this.validStatus = validStatus;
    }

    public ReplyType getReplyType() {
        return replyType;
    }

    public void setReplyType(ReplyType replyType) {
        this.replyType = replyType;
    }

    public String getDestId() {
        return destId;
    }

    public void setDestId(String destId) {
        this.destId = destId;
    }

    public String getReplyObjId() {
        return replyObjId;
    }

    public void setReplyObjId(String replyObjId) {
        this.replyObjId = replyObjId;
    }

    public long getFloorNum() {
        return floorNum;
    }

    public void setFloorNum(long floorNum) {
        this.floorNum = floorNum;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Reply reply = (Reply) o;
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
        return "Reply{" +
            "id=" + getId() +
            ", uid='" + getUid() + "'" +
            ", parentId='" + getParentId() + "'" +
            ", parentUid='" + getParentUid() + "'" +
            ", rootId='" + getRootId() + "'" +
            ", rootUid='" + getRootUid() + "'" +
            ", body='" + getBody() + "'" +
            ", pic='" + getPic() + "'" +
            ", createTime='" + getCreateTime() + "'" +
            ", validStatus='" + getValidStatus() + "'" +
            ", replyType='" + getReplyType() + "'" +
            ", destId='" + getDestId() + "'" +
            ", replyObjId='" + getReplyObjId() + "'" +
            "}";
    }
}
