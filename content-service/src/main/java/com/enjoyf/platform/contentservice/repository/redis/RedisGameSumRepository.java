package com.enjoyf.platform.contentservice.repository.redis;

import com.enjoyf.platform.contentservice.domain.game.GameSum;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

/**
 * Created by zhimingli on 2017/6/21.
 */
@Repository
public class RedisGameSumRepository extends AbstractRedis {

    private static final String PREFIX = "contentservice:";
    private static final String GAME_SUM_KEY = PREFIX + "gamesum:";
    private static final String GAME_RATING_COUNT_HASHKEY = "ratingCount";
    private static final String GAME_RATING_USERCOUNT_HASHKEY = "ratingUserCount";

    private static final String GAME_REOMMEND_HASHKEY = "recommendCount";
    private static final String GAME_REOMMEND_USERCOUNT_HASHKEY = "recommendUserCount";

    public RedisGameSumRepository(StringRedisTemplate redisTemplate) {
        super(redisTemplate);
    }


    public GameSum findOne(Long gameId) {
        GameSum gameSum = null;
        String key = getGameSumKey(gameId);
        if (redisTemplate.opsForHash().size(key) == 0) {
            return gameSum;
        }

        int ratingCount = getIntNumberByKey(key, GAME_RATING_COUNT_HASHKEY);
        int ratingUserCount = getIntNumberByKey(key, GAME_RATING_USERCOUNT_HASHKEY);
        int oneRatingUserCount = getIntNumberByKey(key, getUserCountByRating(1));
        int twoRatingUserCount = getIntNumberByKey(key, getUserCountByRating(2));
        int threeRatingUserCount = getIntNumberByKey(key, getUserCountByRating(3));
        int fourRatingUserCount = getIntNumberByKey(key, getUserCountByRating(4));
        int fiveRatingUserCount = getIntNumberByKey(key, getUserCountByRating(5));

        int recommendCount = getIntNumberByKey(key, GAME_REOMMEND_HASHKEY);
        int recommendUserCount = getIntNumberByKey(key, GAME_REOMMEND_USERCOUNT_HASHKEY);

        gameSum = new GameSum();
        gameSum.setGameId(gameId);
        gameSum.setRatingSum(ratingCount);
        gameSum.setUserCount(ratingUserCount);
        gameSum.setOneUserCount(oneRatingUserCount);
        gameSum.setTwoUserCount(twoRatingUserCount);
        gameSum.setThreeUserCount(threeRatingUserCount);
        gameSum.setFourUserCount(fourRatingUserCount);
        gameSum.setFiveUserCount(fiveRatingUserCount);

        gameSum.setRecommendSum(recommendCount);
        gameSum.setRecommendUserCount(recommendUserCount);

        return gameSum;
    }

    public void addRating(Long gameId, int rating, int userCount) {
        String key = getGameSumKey(gameId);
        redisTemplate.opsForHash().increment(key, GAME_RATING_COUNT_HASHKEY, rating);
        redisTemplate.opsForHash().increment(key, GAME_RATING_USERCOUNT_HASHKEY, userCount);
        redisTemplate.opsForHash().increment(key, getUserCountByRating(rating), userCount);
    }

    public void addRecommendValue(Long gameId, Integer recommend, int userCount) {
        String key = getGameSumKey(gameId);

        redisTemplate.opsForHash().increment(key, GAME_REOMMEND_HASHKEY, recommend);
        redisTemplate.opsForHash().increment(key, GAME_REOMMEND_USERCOUNT_HASHKEY, userCount);
    }


    private int getIntNumberByKey(String key, String hashKey) {
        int res = 0;
        Object resVal = redisTemplate.opsForHash().get(key, hashKey);
        if (resVal != null) {
            res = Integer.parseInt((String) resVal);
        }
        return res;
    }

    private String getGameSumKey(Long gameId) {
        return GAME_SUM_KEY + gameId;
    }

    private String getUserCountByRating(int rating) {
        return rating + GAME_RATING_USERCOUNT_HASHKEY;
    }
}
