package com.enjoyf.platform.contentservice.service.dto.search;

import com.enjoyf.platform.contentservice.domain.game.enumeration.GameOperStatus;
import com.enjoyf.platform.contentservice.domain.game.enumeration.GameType;
import com.enjoyf.platform.json.Timestamp2ZoneDateTimeDeserializer;
import com.enjoyf.platform.json.ZoneDateTime2TimestampSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.time.ZonedDateTime;
import java.util.Set;

/**
 * Created by ericliu on 2017/8/21.
 */
public class GameSearchDTO {
    private Long id;
    private String name;//游戏名称
    private String aliasName;//游戏别名
    private Set<String> gameTags;//游戏标签（中文 分割）
    private GameType gameType=GameType.UNKNOWN;//游戏类型
    private GameOperStatus operStatus = GameOperStatus.UNKNOWN;//运营状态
    private boolean android = false;
    private boolean ios = false;
    private boolean pc = false;
    @JsonSerialize(using = ZoneDateTime2TimestampSerializer.class)
    @JsonDeserialize(using = Timestamp2ZoneDateTimeDeserializer.class)
    private ZonedDateTime createTime;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAliasName() {
        return aliasName;
    }

    public void setAliasName(String aliasName) {
        this.aliasName = aliasName;
    }

    public Set<String> getGameTags() {
        return gameTags;
    }

    public void setGameTags(Set<String> gameTags) {
        this.gameTags = gameTags;
    }

    public GameType getGameType() {
        return gameType;
    }

    public void setGameType(GameType gameType) {
        this.gameType = gameType;
    }

    public GameOperStatus getOperStatus() {
        return operStatus;
    }

    public void setOperStatus(GameOperStatus operStatus) {
        this.operStatus = operStatus;
    }

    public boolean isAndroid() {
        return android;
    }

    public void setAndroid(boolean android) {
        this.android = android;
    }

    public boolean isIos() {
        return ios;
    }

    public void setIos(boolean ios) {
        this.ios = ios;
    }

    public boolean isPc() {
        return pc;
    }

    public void setPc(boolean pc) {
        this.pc = pc;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(ZonedDateTime createTime) {
        this.createTime = createTime;
    }
}
