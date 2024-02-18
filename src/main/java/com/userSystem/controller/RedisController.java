package com.userSystem.controller;

import com.userSystem.Service.RedisService;
import com.userSystem.model.RedisDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController("/redis")
public class RedisController {
    @Autowired
    RedisService redisService;
    @PostMapping("")
    private void saveRedis(RedisDto redisDto){
        redisService.saveData(redisDto);
    }
    @GetMapping("{key}")
    private ResponseEntity<String> getRedisByKey(@PathVariable String key){
        return redisService.getData(key);
    }
    @DeleteMapping("{key}")
    private ResponseEntity<?> deleteRedisByKey(@PathVariable String key){
        return redisService.deleteData(key);
    }
}
