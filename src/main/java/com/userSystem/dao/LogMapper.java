package com.userSystem.dao;

import com.userSystem.model.LogDto;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;


@Mapper
@Repository
public interface LogMapper {
    void insertApiLog(LogDto logDto);
}
