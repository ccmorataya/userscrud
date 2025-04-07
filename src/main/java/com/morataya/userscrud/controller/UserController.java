package com.morataya.userscrud.controller;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.morataya.userscrud.entity.User;
import com.morataya.userscrud.service.UserService;

@RestController
@RequestMapping("/api/users")
public class UserController {

  @Autowired
  private UserService userService;

  @GetMapping
  public List<User> getAllUsers() {
    return userService.getAllUsers();
  }

  @GetMapping("/{userId}")
  public ResponseEntity<?> getById(@PathVariable("userId") Long userId) {
    try {
      Optional<User> user = userService.getUserById(userId);
      if (user.isPresent()) {
        return ResponseEntity.ok(user.get());
      } else {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
      }
    } catch (ResponseStatusException ex) {
      HttpStatus status = HttpStatus.valueOf(ex.getStatusCode().value());

      Map<String, Object> errorResponse = new HashMap<>();
      errorResponse.put("status", status.value());
      errorResponse.put("error", status.getReasonPhrase());
      errorResponse.put("message", ex.getReason());
      errorResponse.put("timestamp", Instant.now().toString());
      return ResponseEntity.status(status).body(errorResponse);
    }
  }

  @PostMapping
  public ResponseEntity<?> saveUser(@RequestBody User user) {
    try {
      userService.save(user);
      return ResponseEntity.status(HttpStatus.CREATED).body(user);
    } catch (ResponseStatusException ex) {
      HttpStatus status = HttpStatus.valueOf(ex.getStatusCode().value());

      Map<String, Object> errorResponse = new HashMap<>();
      errorResponse.put("status", status.value());
      errorResponse.put("error", status.getReasonPhrase());
      errorResponse.put("message", ex.getReason());
      errorResponse.put("timestamp", Instant.now().toString());
      return ResponseEntity.status(status).body(errorResponse);
    }
  }

  @PutMapping("/{userId}")
  public ResponseEntity<?> updateUser(@PathVariable Long userId, @RequestBody User user) {
    try {
      User updatedUser = userService.updateUserById(userId, user);
      return ResponseEntity.ok(updatedUser);
    } catch (ResponseStatusException ex) {
      HttpStatus status = HttpStatus.valueOf(ex.getStatusCode().value());

      Map<String, Object> errorResponse = new HashMap<>();
      errorResponse.put("status", status.value());
      errorResponse.put("error", status.getReasonPhrase());
      errorResponse.put("message", ex.getReason());
      errorResponse.put("timestamp", Instant.now().toString());
      return ResponseEntity.status(status).body(errorResponse);
    }
  }

  @DeleteMapping("/{userId}")
  public ResponseEntity<?> deleteUser(@PathVariable("userId") Long userId) {
    try {
      // User updatedUser = userService.updateUserById(userId, user);
      User deletedUser = userService.deleteUser(userId);
      return ResponseEntity.ok(deletedUser);
    } catch (ResponseStatusException ex) {
      HttpStatus status = HttpStatus.valueOf(ex.getStatusCode().value());

      Map<String, Object> errorResponse = new HashMap<>();
      errorResponse.put("status", status.value());
      errorResponse.put("error", status.getReasonPhrase());
      errorResponse.put("message", ex.getReason());
      errorResponse.put("timestamp", Instant.now().toString());
      return ResponseEntity.status(status).body(errorResponse);
    }
    // userService.deleteUser(userId);
  }

}
