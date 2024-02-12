package com.userSystem.controller;

import com.userSystem.mail.ResponseVo;
import com.userSystem.user.UserService;
import com.userSystem.user.model.UserRo;
import com.userSystem.user.model.UserVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
@ResponseBody
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public UserVo loginUser(@RequestBody UserRo userRo) {
        return userService.loginUser(userRo);
    }

    @PostMapping("")
    public ResponseVo createUser(@RequestBody UserRo userRo) {
       return userService.createUser(userRo);
    }

    @PostMapping("/forgot_password")
    public ResponseVo forgotPassword (@RequestBody UserRo userRo) {
        return userService.forgotPassword(userRo);
    }

    @PutMapping("")
    public void updateUser(@RequestBody UserRo userRo) {
        userService.updateUser(userRo);
    }

    @GetMapping("/{id}")
    public UserRo getUserById(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    @GetMapping("/all")
    public List<UserRo> getAllUsers() {
        return userService.getAllUsers();
    }



    @PutMapping("/{id}")
    public void updateUser(@PathVariable Long id, @RequestBody UserRo userRo) {
        userService.updateUser(id, userRo);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
    }
    @GetMapping("/test")
    public void test (){
    }
}

