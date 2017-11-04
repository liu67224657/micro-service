package com.enjoyf.platform.contentservice.service.dto.game;

/**
 * Created by ericliu on 2017/8/15.
 */
public class GameInfoSimpleDTO {
    private GameSimpleDTO game;
    private GameSumDTO sum;

    public GameSimpleDTO getGame() {
        return game;
    }

    public void setGame(GameSimpleDTO game) {
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
        return "GameSimpleVM{" +
            "game=" + game +
            ", gameRating=" + sum +
            '}';
    }
}
