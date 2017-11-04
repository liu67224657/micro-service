package com.enjoyf.platform.contentservice.service.dto.gamecomment;

import com.enjoyf.platform.contentservice.domain.gamecomment.GameCommentSum;
import com.enjoyf.platform.contentservice.service.userservice.dto.UserProfileSimpleDTO;

/**
 * Created by pengxu on 2017/6/21.
 */
public class GameCommentInfoDTO {
    private UserProfileSimpleDTO profile;
    private GameCommentDTO comment;
    private GameCommentSum sum;
    private boolean hasAgree;

    public UserProfileSimpleDTO getProfile() {
        return profile;
    }

    public void setProfile(UserProfileSimpleDTO profile) {
        this.profile = profile;
    }

    public GameCommentDTO getComment() {
        return comment;
    }

    public void setComment(GameCommentDTO comment) {
        this.comment = comment;
    }

    public GameCommentSum getSum() {
        return sum;
    }

    public void setSum(GameCommentSum sum) {
        this.sum = sum;
    }

    public boolean isHasAgree() {
        return hasAgree;
    }

    public void setHasAgree(boolean hasAgree) {
        this.hasAgree = hasAgree;
    }
}
