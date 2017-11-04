package com.enjoyf.platform.contentservice.domain.gamecomment;

import com.enjoyf.platform.contentservice.domain.gamecomment.enumeration.GameCommentOperType;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A CommentOperation.
 * 点评操作表 2017-07-11 暂时只有对点评点赞操作记录
 */
@Entity
@Table(name = "game_comment_operation")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class GameCommentOperation implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "comment_id")
    private Long commentId;//commentId

    @Column(name = "uid")
    private Long uid;//操作人

    @Column(name = "dest_uid")
    private Long destUid;//被操作人

    @Column(name = "operate_type")
    @Enumerated
    private GameCommentOperType operateType = GameCommentOperType.AGREE;//操作类型

    @Column(name = "create_time")
    private ZonedDateTime createTime = ZonedDateTime.now();

    @Column(name = "update_time")
    private ZonedDateTime updateTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCommentId() {
        return commentId;
    }

    public GameCommentOperation commentId(Long commentId) {
        this.commentId = commentId;
        return this;
    }

    public void setCommentId(Long commentId) {
        this.commentId = commentId;
    }

    public Long getUid() {
        return uid;
    }

    public GameCommentOperation uid(Long uid) {
        this.uid = uid;
        return this;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public Long getDestUid() {
        return destUid;
    }

    public GameCommentOperation destUid(Long destUid) {
        this.destUid = destUid;
        return this;
    }

    public void setDestUid(Long destUid) {
        this.destUid = destUid;
    }

    public GameCommentOperType getOperateType() {
        return operateType;
    }

    public GameCommentOperation operateType(GameCommentOperType operateType) {
        this.operateType = operateType;
        return this;
    }

    public void setOperateType(GameCommentOperType operateType) {
        this.operateType = operateType;
    }

    public ZonedDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(ZonedDateTime createTime) {
        this.createTime = createTime;
    }

    public ZonedDateTime getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(ZonedDateTime updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        GameCommentOperation gameCommentOperation = (GameCommentOperation) o;
        if (gameCommentOperation.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), gameCommentOperation.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "CommentOperation{" +
            "id=" + getId() +
            ", commentId='" + getCommentId() + "'" +
            ", uid='" + getUid() + "'" +
            ", destUid='" + getDestUid() + "'" +
            ", operateType='" + getOperateType() + "'" +
            ", createTime='" + getCreateTime() + "'" +
            ", updateTime='" + getUpdateTime() + "'" +
            "}";
    }
}
