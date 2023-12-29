package com.example.userservice.controller;

import com.example.userservice.model.UserDto;
import com.example.userservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/get-all-users")
    public ResponseEntity<List<UserDto>> getAll(){
        return new ResponseEntity<> (userService.getUsers(), HttpStatus.OK);
    }

    @PutMapping("/change-user-status/{id}")
    public void changeStatus(@PathVariable("id") Long id){
        userService.changeStatusUser(id);
    }

    @PutMapping("/change-info")
    public void changeInfo(@RequestBody UserDto user){
        userService.changeUserInfo(user);
    }

    @PostMapping("/add-user")
    public ResponseEntity<Long> addUser(@RequestBody UserDto user){
        return new ResponseEntity<>(userService.createUser(user), HttpStatus.OK);
    }

    @GetMapping("/exist-by-id/{userId}")
    public boolean existById(@PathVariable("userId") Long userId){
        return userService.findUser(userId);
    }

    @GetMapping("/name-by-id/{id}")
    public String getNameById(@PathVariable("id") Long id){
        return userService.getUserName(id);
    }

    @GetMapping("/exist-by-id-for-company/{userId}")
    public boolean existByIdCompany(@PathVariable("userId") Long userId){
        return userService.findUser(userId);
    }
}
