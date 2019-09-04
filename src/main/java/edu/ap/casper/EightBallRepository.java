package edu.ap.casper;

import edu.ap.casper.model.EightBall;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class EightBallRepository {

    @Autowired
    private RedisTemplate<String, EightBall> EightBallAnswerRedisTemplate;

    public void saveQuestion(EightBall eightBall) {
        EightBallAnswerRedisTemplate.boundListOps("question").rightPush(eightBall);
    }

    public List<EightBall> getAll() {

        return EightBallAnswerRedisTemplate.boundListOps("question").range(0,- 1);
    }

}

