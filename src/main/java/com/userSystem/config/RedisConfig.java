package com.userSystem.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.Jedis;


@Configuration
public class RedisConfig {
    @Bean
    public Jedis jedis() {
        // Set up Jedis connection parameters as needed
        return new Jedis("localhost",6379);
    }

}
