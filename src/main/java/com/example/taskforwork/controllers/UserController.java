package com.example.taskforwork.controllers;

import com.example.taskforwork.dto.AuthenticationController;
import com.example.taskforwork.entities.Email;
import com.example.taskforwork.entities.PhoneNumber;
import com.example.taskforwork.entities.User;
import com.example.taskforwork.jwt.JwtUtil;
import com.example.taskforwork.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    @Autowired
    public UserController(UserService userService, AuthenticationManager authenticationManager, JwtUtil jwtUtil) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@RequestBody User user) {
        User createdUser = userService.registerUser(user);
        return ResponseEntity.ok(createdUser);
    }

    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@RequestBody AuthenticationController loginRequest) {
        try {
            // Perform authentication using username and password
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
            );
            // Generate JWT token
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String jwtToken = jwtUtil.generateToken(userDetails.getUsername());
            return ResponseEntity.ok(jwtToken);
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
        }
    }

    @GetMapping("/info")
    public ResponseEntity<User> getUserInformation(@AuthenticationPrincipal UserDetails userDetails) {
        String username = userDetails.getUsername();
        User user = userService.getUserByLogin(username);
        return ResponseEntity.ok(user);
    }

    @PostMapping("/addPhoneNumber/{userId}/phone")
    public ResponseEntity<User> addPhoneNumber(@PathVariable Long userId, @RequestBody PhoneNumber phoneNumber) {
        User user = userService.addPhoneNumber(userId, phoneNumber);
        return ResponseEntity.ok(user);
    }

    @PostMapping("/addEmail/{userId}/email")
    public ResponseEntity<User> addEmail(@PathVariable Long userId, @RequestBody Email email) {
        User user = userService.addEmail(userId, email);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<Optional<User>> getUserById(@PathVariable Long userId) {
        Optional<User> user = userService.getUserById(userId);
        if (user.isPresent()) {
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/list")
    public ResponseEntity<List<User>> getUsersList(@RequestParam(defaultValue = "0") int page,
                                                   @RequestParam(defaultValue = "10") int size) {
        Page<User> usersPage = userService.getAllUsers(page, size);
        List<User> usersList = usersPage.getContent();
        return ResponseEntity.ok(usersList);
    }

    @PutMapping("/updateUser/{userId}")
    public ResponseEntity<User> updateUserById(@PathVariable Long userId, @RequestBody User updatedUser) {
        User user = userService.updateUser(userId, updatedUser);
        if (user != null) {
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/updateUserEmail/{userId}/email")
    public ResponseEntity<User> updateUserEmailById(@PathVariable Long userId, @RequestBody Email updatedEmail) {
        User user = userService.updateUserEmailById(userId, updatedEmail);
        if (user != null) {
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/updateUserPhoneNumber/{userId}/phone")
    public ResponseEntity<User> updateUserPhoneNumberById(@PathVariable Long userId, @RequestBody PhoneNumber updatedPhoneNumber) {
        User user = userService.updateUserPhoneNumberById(userId, updatedPhoneNumber);
        if (user != null) {
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/delete/{userId}")
    public ResponseEntity<Void> deleteUserById(@PathVariable Long userId) {
        boolean deleted = userService.deleteUserById(userId);
        if (deleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}
