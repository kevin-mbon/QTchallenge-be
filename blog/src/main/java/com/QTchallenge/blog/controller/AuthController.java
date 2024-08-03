package com.QTchallenge.blog.controller;

import com.QTchallenge.blog.dto.UserDTO;
import com.QTchallenge.blog.model.Users;
import com.QTchallenge.blog.services.UserService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserService userService;


    @ApiOperation(value = "Get all users")
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody UserDTO userDTO) {
        if (userService.findByUsername(userDTO.getUsername()).isPresent()) {
            return ResponseEntity.badRequest().body("Username is already taken");
        }

        UserDTO savedUserDTO = userService.save(userDTO);
        return ResponseEntity.ok(savedUserDTO); // Password will not be included
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody Users user) {
        String token = userService.login(user.getUsername(), user.getPassword());
        if (token != null) {
            return ResponseEntity.ok(token);
        } else {
            return ResponseEntity.badRequest().body("Invalid username or password");
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logoutUser(@RequestHeader("Authorization") String token) {
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
            if (userService.logout(token)) {
                return ResponseEntity.ok("Logged out successfully");
            } else {
                return ResponseEntity.status(500).body("Logout failed");
            }
        } else {
            return ResponseEntity.badRequest().body("Invalid token");
        }
    }

    @GetMapping
    public List<UserDTO> getAllUsers() {
        return userService.findAll();
    }
}
