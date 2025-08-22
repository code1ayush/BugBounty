package com.Ayush.BugBounty.Controller;

import com.Ayush.BugBounty.Entity.User;
import com.Ayush.BugBounty.Service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService){
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<?> updateUser(@RequestBody User user){
        try{
            String userName = SecurityContextHolder.getContext().getAuthentication().getName();
            userService.updateUser(userName,user);
            return new ResponseEntity<>(HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping
    public ResponseEntity<?> deleteUser(){
        try{
            String userName = SecurityContextHolder.getContext().getAuthentication().getName();
            userService.deleteUser(userName);
            return new ResponseEntity<>(userName + "is deleted",HttpStatus.OK);
        }catch (Exception e){
            throw new UsernameNotFoundException("userName not found",e);
        }
    }
}
