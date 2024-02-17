package com.userSystem.Service;

import com.userSystem.dao.LogMapper;
import com.userSystem.dao.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
@Slf4j
public class InitTableService {
    private UserMapper userMapper;
    private LogMapper logMapper;

    @Autowired
    InitTableService(UserMapper userMapper, LogMapper logMapper) {
        this.userMapper = userMapper;
        this.logMapper = logMapper;
    }

    @PostConstruct
    private void initUser() {
        try {
            initializeTable("user_system", "user");
        } catch (Exception e) {
            log.error("Exception in method: " + Thread.currentThread().getStackTrace()[1].getMethodName(), e);
        }
    }

    @PostConstruct
    private void initApiLog() {
        try {
            initializeTable("user_system", "api_log");
        } catch (Exception e) {
            log.error("Exception in method: " + Thread.currentThread().getStackTrace()[1].getMethodName(), e);
        }
    }

    private void initializeTable(String dbName, String tableName) {
        log.info("Checking if the " + dbName + " exists.");
        userMapper.createDatabase(dbName);
        int checkCount = userMapper.checkTableExistence(dbName, tableName);

        if (checkCount == 0) {
            log.info("Table does not exist. Creating the table.");
            if ("user".equals(tableName)) {
                userMapper.createUserSystem();
            } else if ("api_log".equals(tableName)) {
                logMapper.createApiLog();
            }
            log.info("Table created successfully.");
        } else {
            log.info("Table already exists. No action needed.");
        }
    }

}
