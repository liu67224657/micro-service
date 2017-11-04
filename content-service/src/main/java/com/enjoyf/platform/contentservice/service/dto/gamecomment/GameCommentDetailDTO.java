package com.enjoyf.platform.contentservice.service.dto.gamecomment;

import com.enjoyf.platform.contentservice.service.userservice.dto.UserProfileDTO;
import com.enjoyf.platform.contentservice.service.dto.game.GameInfoSimpleDTO;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pengxu on 2017/6/21.
 */
public class GameCommentDetailDTO {
    private GameInfoSimpleDTO game;//所属游戏
    private GameCommentDTO comment;//点评内容
    private List<UserProfileDTO> profile = new ArrayList<>();//点赞用户列表

    public GameInfoSimpleDTO getGame() {
        return game;
    }

    public void setGame(GameInfoSimpleDTO game) {
        this.game = game;
    }

    public GameCommentDTO getComment() {
        return comment;
    }

    public void setComment(GameCommentDTO comment) {
        this.comment = comment;
    }

    public List<UserProfileDTO> getProfile() {
        return profile;
    }

    public void setProfile(List<UserProfileDTO> profile) {
        this.profile = profile;
    }
}
