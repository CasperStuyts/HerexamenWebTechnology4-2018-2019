package edu.ap.casper;

import edu.ap.casper.model.EightBall;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import java.net.UnknownHostException;

@SpringBootApplication
public class CasperApplication {

    @Autowired
    private EightBallRepository EightBallAnswerRedisTemplate;

    @Bean
    @ConditionalOnMissingBean
    public RedisConnectionFactory redisConnectionFactory()
            throws UnknownHostException {
        JedisConnectionFactory factory =
                new JedisConnectionFactory();
        factory.setHostName("localhost");
        factory.setPort(6379);
        return factory;
    }

    @Bean
    @ConditionalOnMissingBean(name = "redisTemplate")
    public RedisOperations<Object, Object> redisTemplate(
            RedisConnectionFactory redisConnectionFactory)
            throws UnknownHostException {
        RedisTemplate<Object, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory);
        return template;
    }


    @Bean
    public RedisTemplate<String, EightBall> getEightBallAnswerRedisTemplate(
            RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, EightBall> t = new RedisTemplate<>();
        t.setConnectionFactory(redisConnectionFactory);
        t.setKeySerializer(new StringRedisSerializer());
        t.setValueSerializer(new Jackson2JsonRedisSerializer<>(EightBall.class));
        t.afterPropertiesSet();
        return t;
    }


    @Bean
    public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
        return (args) -> {

			EightBallAnswerRedisTemplate.saveQuestion(new EightBall("test","test"));
            EightBallAnswerRedisTemplate.saveQuestion(new EightBall("test2","test"));
            EightBallAnswerRedisTemplate.saveQuestion(new EightBall("test5","test"));
            EightBallAnswerRedisTemplate.getAll().forEach(s -> System.out.println(s.getAnswer()));

        };
    }

    public static void main(String[] args) {
        SpringApplication.run(CasperApplication.class, args);
    }

}
