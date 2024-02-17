package com.userSystem.controller;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.userSystem.model.ResponseVo;
import com.userSystem.Service.UserService;
import com.userSystem.model.UserRo;
import com.userSystem.model.UserVo;
import org.apache.catalina.connector.Request;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/user")
@ResponseBody
public class UserController {
    private UserService userService;
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/login")
    public UserVo loginUser(@RequestBody UserRo userRo, HttpServletRequest request) {
        return userService.loginUser(userRo,request );
    }

    @PostMapping("")
    public ResponseVo createUser(@RequestBody UserRo userRo,HttpServletRequest request) {
       return userService.createUser(userRo,request);
    }

    @PostMapping("/forgot_password")
    public ResponseVo forgotPassword (@RequestBody UserRo userRo) {
        return userService.forgotPassword(userRo);
    }

    @PutMapping("")
    public ResponseVo updateUser(@RequestBody UserRo userRo,HttpServletRequest request) {
        return userService.updateUser(userRo,request );
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

