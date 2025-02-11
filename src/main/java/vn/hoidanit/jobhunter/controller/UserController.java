package vn.hoidanit.jobhunter.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import vn.hoidanit.jobhunter.domain.User;
import vn.hoidanit.jobhunter.service.UserService;
import vn.hoidanit.jobhunter.util.error.IdInvalidException;

import java.util.List;

@RestController
public class UserController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public UserController(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(this.userService.fetchAllUser());
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<User> getAllUsers(@PathVariable("id") long id) {
        return  ResponseEntity.ok(this.userService.fetchUserById(id));
    }

    @PostMapping("/users")
    public ResponseEntity<User> createNewUser(@RequestBody User user) {
        String hashPassword = this.passwordEncoder.encode(user.getPassword());
        user.setPassword(hashPassword);
        User userCreate = this.userService.handleCreateUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(userCreate);
    }

    @PutMapping("/users")
    public ResponseEntity<User> updateUser(@RequestBody User user) {
        return  ResponseEntity.ok(this.userService.handleUpdateUser(user));
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<String> deleteUserById(@PathVariable("id") long id) throws IdInvalidException {
        if(id >=1500) {
            throw new IdInvalidException("id must be less than or equal to 1500");
        }
        this.userService.handleDeleteUser(id);

        return  ResponseEntity.noContent().build();
    }
}
