package com.userSystem.Service;

import com.userSystem.dao.RedisDao;
import com.userSystem.model.RedisDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class RedisService {

    private final RedisDao redisDao;

    public RedisService(RedisDao redisDao) {
        this.redisDao = redisDao;
    }

    public void saveData(RedisDto redisDto) {
        try {
            redisDao.saveData(redisDto);
        } catch (Exception e) {
            log.error("Error saving data to Redis", e);
            // Handle the exception as needed
        }
    }

    public ResponseEntity<String> getData(String key) {
        try {
            return ResponseEntity.ok().body(redisDao.getData(key));
        } catch (Exception e) {
            log.error("Error retrieving data from Redis", e);
            // Handle the exception as needed
            return null;
        }
    }

    public ResponseEntity<?> deleteData(String key) {
        if (redisDao.deleteData(key)) {
            return ResponseEntity.ok().build();
        } else {
            log.error("delete fail.");
            return ResponseEntity.badRequest().build();
        }
    }
}

