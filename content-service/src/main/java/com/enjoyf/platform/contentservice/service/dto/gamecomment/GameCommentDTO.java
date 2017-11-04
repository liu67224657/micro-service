package com.enjoyf.platform.contentservice.service.dto.gamecomment;

import com.enjoyf.platform.common.domain.enumeration.ValidStatus;

import java.time.ZonedDateTime;

/**
 * Created by pengxu on 2017/6/21.
 */
public class GameCommentDTO {
    private Long id;
    private Long gameId;
    private Long uid;
    private Integer rating = 0;
    private Integer recommendValue = 0;
    private String body;
    private ZonedDateTime createTime = ZonedDateTime.now();
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

    public Integer getHighQuality() {
        return highQuality;
    }

    public void setHighQuality(Integer highQuality) {
        this.highQuality = highQuality;
    }
}
