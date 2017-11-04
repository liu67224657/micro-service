package com.enjoyf.platform.contentservice.domain.game;

import com.enjoyf.platform.common.domain.enumeration.ValidStatus;
import com.enjoyf.platform.contentservice.domain.convert.GameExtJsonConvert;
import com.enjoyf.platform.contentservice.domain.game.enumeration.GameOperStatus;
import com.enjoyf.platform.contentservice.domain.game.enumeration.GameType;
import com.enjoyf.platform.json.Timestamp2ZoneDateTimeDeserializer;
import com.enjoyf.platform.json.ZoneDateTime2TimestampSerializer;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.redis.core.RedisHash;

import javax.persistence.*;
import javax.persistence.Id;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A Game.
 */
@Entity
@Table(name = "game")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@RedisHash("game")
public class Game implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @org.springframework.data.annotation.Id
    private Long id;

    @Column(name = "name")
    private String name;//游戏名称


    @Column(name = "alias_name", nullable = false, columnDefinition = "")
    private String aliasName;//游戏别名

    @Column(name = "game_tag", nullable = false, columnDefinition = "")
    private String gameTag;//游戏标签

    @Column(name = "game_type", nullable = false)
    @Enumerated(EnumType.ORDINAL)
    private GameType gameType;//游戏类型

    @Column(name = "oper_status", nullable = false)
    @Enumerated(EnumType.ORDINAL)
    private GameOperStatus operStatus = GameOperStatus.UNKNOWN;//运营状态

    @Column(name = "ext_json", nullable = false)
    @Convert(converter = GameExtJsonConvert.class)
    @JsonProperty(value = "extjson")
    private GameExtJson extJson;

    @Column(name = "valid_status", nullable = false)
    @Enumerated
    private ValidStatus validStatus = ValidStatus.VALID;

    @Column(name = "create_time", nullable = false)
    @JsonSerialize(using = ZoneDateTime2TimestampSerializer.class)
    @JsonDeserialize(using = Timestamp2ZoneDateTimeDeserializer.class)
    private ZonedDateTime createTime = ZonedDateTime.now();

    @Column(name = "is_android", nullable = false)
    private boolean android = false; //

    @Column(name = "is_ios", nullable = false)
    private boolean ios = false; //

    @Column(name = "is_pc", nullable = false)
    private boolean pc = false; //

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Game name(String name) {
        this.name = name;
        return this;
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

    public String getGameTag() {
        return gameTag;
    }

    public void setGameTag(String gameTag) {
        this.gameTag = gameTag;
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

    public GameExtJson getExtJson() {
        return extJson;
    }

    public void setExtJson(GameExtJson extJson) {
        this.extJson = extJson;
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

    public void setCreateTime(ZonedDateTime createTime) {
        this.createTime = createTime;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Game game = (Game) o;
        if (game.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), game.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Game{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", aliasName='" + getAliasName() + "'" +
            ", gameTag='" + getGameTag() + "'" +
            ", extJson='" + getExtJson() + "'" +
            ", validStatus='" + getValidStatus() + "'" +
            ", createTime='" + getCreateTime() + "'" +
            "}";
    }
}
