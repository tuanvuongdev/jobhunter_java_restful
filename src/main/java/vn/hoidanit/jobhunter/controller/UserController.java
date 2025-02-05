package vn.hoidanit.jobhunter.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import vn.hoidanit.jobhunter.domain.User;
import vn.hoidanit.jobhunter.service.UserService;

import java.util.List;

@RestController
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/user")
    public List<User> getAllUsers() {
        return this.userService.fetchAllUser();
    }

    @GetMapping("/user/{id}")
    public User getAllUsers(@PathVariable long id) {
        return this.userService.fetchUserById(id);
    }

    @PostMapping("/user")
    public User createNewUser(@RequestBody User user) {
        return this.userService.handleSaveUser(user);
    }


    @PutMapping("/user")
    public User updateUser(@RequestBody User user) {

        return this.userService.handleUpdateUser(user);
    }

    @DeleteMapping("/user/{userId}")
    public String createNewUser(@PathVariable long userId) {
        this.userService.handleDeleteUser(userId);

        return "delete user";
    }
}
