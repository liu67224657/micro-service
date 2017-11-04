package com.enjoyf.platform.contentservice.service.dto.game;

/**
 * Created by ericliu on 2017/8/15.
 */
public class GameSumDTO {
    private int ratingSum = 0;
    private int userCount;

    private Integer recommendSum =0;
    private Integer recommendUserCount=0;

    private Integer fiveUserCount = 0;
    private Integer fourUserCount = 0;
    private Integer threeUserCount = 0;
    private Integer twoUserCount = 0;
    private Integer oneUserCount = 0;

    public int getRatingSum() {
        return ratingSum;
    }

    public void setRatingSum(int ratingSum) {
        this.ratingSum = ratingSum;
    }

    public int getUserCount() {
        return userCount;
    }

    public void setUserCount(int userCount) {
        this.userCount = userCount;
    }

    public Integer getFiveUserCount() {
        return fiveUserCount;
    }

    public void setFiveUserCount(Integer fiveUserCount) {
        this.fiveUserCount = fiveUserCount;
    }

    public Integer getFourUserCount() {
        return fourUserCount;
    }

    public void setFourUserCount(Integer fourUserCount) {
        this.fourUserCount = fourUserCount;
    }

    public Integer getThreeUserCount() {
        return threeUserCount;
    }

    public void setThreeUserCount(Integer threeUserCount) {
        this.threeUserCount = threeUserCount;
    }

    public Integer getTwoUserCount() {
        return twoUserCount;
    }

    public void setTwoUserCount(Integer twoUserCount) {
        this.twoUserCount = twoUserCount;
    }

    public Integer getOneUserCount() {
        return oneUserCount;
    }

    public void setOneUserCount(Integer oneUserCount) {
        this.oneUserCount = oneUserCount;
    }

    public Integer getRecommendSum() {
        return recommendSum;
    }

    public void setRecommendSum(Integer recommendSum) {
        this.recommendSum = recommendSum;
    }

    public Integer getRecommendUserCount() {
        return recommendUserCount;
    }

    public void setRecommendUserCount(Integer recommendUserCount) {
        this.recommendUserCount = recommendUserCount;
    }

    @Override
    public String toString() {
        return "RatingDTO{" +
            "ratingCount=" + ratingSum +
            ", userCount=" + userCount +
            ", recommendCount=" + recommendSum +
            ", recommendUserCount=" + recommendUserCount +
            ", fiveUserCount=" + fiveUserCount +
            ", fourUserCount=" + fourUserCount +
            ", threeUserCount=" + threeUserCount +
            ", twoUserCount=" + twoUserCount +
            ", oneUserCount=" + oneUserCount +
            '}';
    }
}
