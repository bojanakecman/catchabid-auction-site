package at.ac.ase.redis;

import at.ac.ase.entities.Bid;
import at.ac.ase.redis.model.HighestBid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
@EnableRedisRepositories
public class RedisConfig {

    @Value("${spring.redis.host}")
    String hostName;

    @Value("${spring.redis.port}")
    Integer port;


    @Bean
    JedisConnectionFactory jedisConnectionFactory() {
        RedisStandaloneConfiguration config = new RedisStandaloneConfiguration(hostName, port);
        return new JedisConnectionFactory(config);
    }


    @Bean
    public RedisTemplate<Long, HighestBid> redisTemplate() {
        RedisTemplate<Long, HighestBid> template = new RedisTemplate<>();
        template.setConnectionFactory(jedisConnectionFactory());
        template.setEnableTransactionSupport(true);

        return template;

    }

}
