package com.userSystem.dao;

import com.userSystem.model.UserBo;
import com.userSystem.model.UserRo;
import com.userSystem.model.UserVo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface UserMapper {
    void insertUser(UserBo userBo);
    UserVo selectUserByAccountPassword(UserBo userBo);
    void updateUser(UserBo userBo);

    void updateUserById(UserRo userRo);

    void deleteUserById(Long id);

    UserRo selectUserById(Long id);


    List<UserRo> selectAllUser();

}
