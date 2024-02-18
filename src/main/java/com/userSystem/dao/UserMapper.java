package com.userSystem.dao;

import com.userSystem.model.UserBo;
import com.userSystem.model.UserRo;
import com.userSystem.model.UserVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface UserMapper {
    void insertUser(UserBo userBo);

    UserVo selectUserByAccountPassword(UserBo userBo);

    UserVo selectUserByAccountOrEmail(UserBo userBo);

    void updateUser(UserBo userBo);

    void updatePassword(@Param("userBo") UserBo userBo, @Param("email") String email);

    void updateUserById(UserRo userRo);

    void deleteUserById(Long id);

    UserRo selectUserById(Long id);

    List<UserRo> selectAllUser();

    void createDatabase(@Param("dbName") String dbName);

    void createUserSystemTable();

    Integer checkTableExistence(@Param("dbName") String dbName, @Param("tableName") String tableName);
}
