package com.enjoyf.platform.contentservice.domain.gamecomment;

import com.enjoyf.platform.common.domain.enumeration.ValidStatus;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.redis.core.RedisHash;

import javax.persistence.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A Comment.
 */
@Entity
@Table(name = "game_comment")
@RedisHash("game_comment")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class GameComment implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @org.springframework.data.annotation.Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "game_id")
    private Long gameId;

    @Column(name = "uid")
    private Long uid;

    @Column(name = "rating")
    private Integer rating = 0;

    @Column(name = "recommend_value")
    private Integer recommendValue = 0;

    @Column(name = "body")
    private String body;

    @Column(name = "create_time")
    private ZonedDateTime createTime = ZonedDateTime.now();

    @Column(name = "modify_time")
    private ZonedDateTime modifyTime;

    @Column(name = "valid_status")
    @Enumerated
    private ValidStatus validStatus = ValidStatus.VALID;

    @Column(name = "high_quality")
    private Integer highQuality = 0;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getGameId() {
        return gameId;
    }

    public void setGameId(Long gameId) {
        this.gameId = gameId;
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public Integer getRecommendValue() {
        return recommendValue;
    }

    public void setRecommendValue(Integer recommendValue) {
        this.recommendValue = recommendValue;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public ZonedDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(ZonedDateTime createTime) {
        this.createTime = createTime;
    }

    public ZonedDateTime getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(ZonedDateTime modifyTime) {
        this.modifyTime = modifyTime;
    }

    public ValidStatus getValidStatus() {
        return validStatus;
    }

    public void setValidStatus(ValidStatus validStatus) {
        this.validStatus = validStatus;
    }

    public Integer getHighQuality() {
        return highQuality;
    }

    public void setHighQuality(Integer highQuality) {
        this.highQuality = highQuality;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        GameComment comment = (GameComment) o;
        if (comment.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), comment.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }


    @Override
    public String toString() {
        return "GameComment{" +
            "id=" + id +
            ", gameId=" + gameId +
            ", uid=" + uid +
            ", rating=" + rating +
            ", recommendValue=" + recommendValue +
            ", body='" + body + '\'' +
            ", createTime=" + createTime +
            ", modifyTime=" + modifyTime +
            ", validStatus=" + validStatus +
            ", highQuality=" + highQuality +
            '}';
    }
}
