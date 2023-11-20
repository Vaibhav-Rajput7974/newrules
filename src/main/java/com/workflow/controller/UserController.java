package com.workflow.controller;

import com.workflow.entity.CustomResponseEntity;
import com.workflow.entity.User;
import com.workflow.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<?> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return ResponseEntity.status(HttpStatus.OK).body(
                new CustomResponseEntity<>("list of users",500,users)
        );
    }

    public ResponseEntity<?> getUserById(@PathVariable long userId) {
        Optional<User> optionalUser = userService.getUserById(userId);

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            return ResponseEntity.status(HttpStatus.OK).body(
                    new CustomResponseEntity<>("list of all the user",500,user)
            );
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new CustomResponseEntity<>("user Not found",404,null)
            );
        }
    }

    @PostMapping
    public ResponseEntity<User> addNewUser(@PathVariable long projectId , @RequestBody User addUser) {
        User savedUser = userService.addNewUser(addUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);
    }

    @PutMapping("/{userId}")
    public ResponseEntity<User> updateUser(@PathVariable long projectId , @PathVariable long userId, @RequestBody User updateUser) {
        Optional<User> optionalUser = userService.updateUser(userId, updateUser);

        if (optionalUser.isPresent()) {
            return ResponseEntity.ok(optionalUser.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUserById(@PathVariable long projectId ,  @PathVariable long userId) {
        if (userService.deleteUserById(userId)) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}