package com.userSystem.user;

import com.userSystem.user.model.UserRo;
import com.userSystem.user.model.UserVo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface UserMapper {
    void insertUser(UserRo userRo);
    void updateUser(UserRo userRo);

    void updateUserById(UserRo userRo);

    void deleteUserById(Long id);

    UserRo selectUserById(Long id);
    UserVo selectUserByAccountPassword(UserRo userRo);

    List<UserRo> selectAllUser();

}
