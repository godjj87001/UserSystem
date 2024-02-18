package com.userSystem.dao;

import com.userSystem.model.RedisDto;
import org.springframework.stereotype.Repository;
import redis.clients.jedis.Jedis;

@Repository
public class RedisDao {
    private final Jedis jedis;

    public RedisDao(Jedis jedis) {
        this.jedis = jedis;
    }

    public void saveData(RedisDto redisDto) {
        // Implement your logic to save data to Redis
        jedis.set(redisDto.getKey(), redisDto.getValue());
    }

    public String getData(String key) {
        // Implement your logic to retrieve data from Redis
        return jedis.get(key);
    }

    public boolean deleteData(String key) {
        boolean isDelete;
        Long deletedCount = jedis.del(key);
        if (deletedCount > 0) {
            isDelete = true;
        } else {
            isDelete = false;
        }
        return isDelete;
    }
}
