package com.enjoyf.platform.contentservice.service.dto.game;

/**
 * Created by ericliu on 2017/8/15.
 */
public class GameInfoDTO {
    private GameDTO game;
    private GameSumDTO sum;

    public GameDTO getGame() {
        return game;
    }

    public void setGame(GameDTO game) {
        this.game = game;
    }

    public GameSumDTO getSum() {
        return sum;
    }

    public void setSum(GameSumDTO sum) {
        this.sum = sum;
    }

    @Override
    public String toString() {
        return "GameVM{" +
            "game=" + game +
            ", gameRatingDTO=" + sum +
            '}';
    }
}
