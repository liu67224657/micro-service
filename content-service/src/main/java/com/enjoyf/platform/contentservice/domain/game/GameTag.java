package com.enjoyf.platform.contentservice.domain.game;

import com.enjoyf.platform.common.domain.enumeration.ValidStatus;
import io.swagger.annotations.ApiModel;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A GameTag.
 */
@Entity
@Table(name = "game_tag")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@RedisHash("gametag")
@ApiModel
public class GameTag implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @org.springframework.data.annotation.Id
    private Long id;

    @NotNull
    @Column(name = "tag_name")
    @Indexed
    private String tagName;

    @Column(name = "game_sum", nullable = false)
    private Integer gameSum = 0;

    @Column(name = "recommend_status", nullable = false)
    private Integer recommendStatus = 0;

    @Column(name = "valid_status", nullable = false)
    @Enumerated
    private ValidStatus validStatus = ValidStatus.VALID;

    @Column(name = "create_time", nullable = false)
    private ZonedDateTime createTime = ZonedDateTime.now();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTagName() {
        return tagName;
    }

    public GameTag tagName(String tagName) {
        this.tagName = tagName;
        return this;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public Integer getGameSum() {
        return gameSum;
    }

    public GameTag gameSum(Integer gameSum) {
        this.gameSum = gameSum;
        return this;
    }

    public void setGameSum(Integer gameSum) {
        this.gameSum = gameSum;
    }

    public Integer getRecommendStatus() {
        return recommendStatus;
    }

    public GameTag recommendStatus(Integer recommendStatus) {
        this.recommendStatus = recommendStatus;
        return this;
    }

    public void setRecommendStatus(Integer recommendStatus) {
        this.recommendStatus = recommendStatus;
    }

    public ValidStatus getValidStatus() {
        return validStatus;
    }

    public void setValidStatus(ValidStatus validStatus) {
        this.validStatus = validStatus;
    }

    public ZonedDateTime getCreateTime() {
        return createTime;
    }

    public GameTag createTime(ZonedDateTime createTime) {
        this.createTime = createTime;
        return this;
    }

    public void setCreateTime(ZonedDateTime createTime) {
        this.createTime = createTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        GameTag gameTag = (GameTag) o;
        if (gameTag.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), gameTag.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "GameTag{" +
            "id=" + getId() +
            ", tagName='" + getTagName() + "'" +
            ", gameSum='" + getGameSum() + "'" +
            ", recommendStatus='" + getRecommendStatus() + "'" +
            ", validStatus='" + getValidStatus() + "'" +
            ", createTime='" + getCreateTime() + "'" +
            "}";
    }
}
