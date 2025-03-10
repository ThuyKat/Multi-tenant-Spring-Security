package multi_tenant.db.navigation.Config;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;




@Configuration
public class RedisConfig {
    @Value("${redis.host}")
    String redisDataSource;

    @Value("${redis.port}")
    int redisDsPort;

    @Value("${redis.password}")
    String redisDsPassword;

    @Bean
    public LettuceConnectionFactory lettuceRedisConnectionFactory() {
        RedisStandaloneConfiguration redisStandaloneConfiguration = 
            new RedisStandaloneConfiguration(redisDataSource, redisDsPort);
        redisStandaloneConfiguration.setPassword(redisDsPassword);
        LettuceConnectionFactory lettuceRedisConnectionFactory = 
            new LettuceConnectionFactory(redisStandaloneConfiguration);
        return lettuceRedisConnectionFactory;
    }

    @Bean
    RedisTemplate<String, Object> redisTemplate() {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(lettuceRedisConnectionFactory());
        
        // Add serializers for keys and values
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        /*
         * Converts Java objects to JSON format before storing them in Redis
        	Uses Object.class as the target type, meaning it can serialize any Java object 
        */
        redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(Object.class));
        
        return redisTemplate;
    }
    
    // Add this to enable @Cacheable, @CachePut annotations if needed
    @Bean
    public CacheManager cacheManager() {
        RedisCacheConfiguration cacheConfig = RedisCacheConfiguration.defaultCacheConfig()
            .entryTtl(Duration.ofHours(1)) // Set TTL to 1 hour
            .disableCachingNullValues();
            
        return RedisCacheManager.builder(lettuceRedisConnectionFactory())
            .cacheDefaults(cacheConfig)
            .build();
    }
}
