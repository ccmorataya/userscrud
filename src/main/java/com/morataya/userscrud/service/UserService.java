package com.morataya.userscrud.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.morataya.userscrud.entity.User;
import com.morataya.userscrud.repository.UserRepository;

@Service
public class UserService {
  @Autowired
  private UserRepository userRepository;

  @Autowired
  private BCryptPasswordEncoder passwordEncoder;

  public List<User> getAllUsers() {
    return userRepository.findAll();
  }

  public Optional<User> getUserById(Long userId) {
    Optional<User> user = Optional.of(userRepository.findById(userId)
        .orElseThrow(() -> {
          throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }));
    return user;
  }

  public void save(User user) {
    if (user.getFirstName() == null || user.getFirstName().isEmpty() ||
        user.getLastName() == null || user.getLastName().isEmpty() ||
        user.getEmail() == null || user.getEmail().isEmpty() ||
        user.getStatus() == null || 
        user.getPassword() == null || user.getPassword().isEmpty()) {
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Missing required fields");
    }

    if (userRepository.findByEmail(user.getEmail()).isPresent()) {
        throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already exists");
    }
    user.setPassword(passwordEncoder.encode(user.getPassword()));
    userRepository.save(user);
  }

  public User updateUserById(Long userId, User user) {
    User existingUser = userRepository.findById(userId)
        .orElseThrow(() -> {
          throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid user");
        });

    existingUser.setFirstName(user.getFirstName());
    existingUser.setLastName(user.getLastName());
    existingUser.setEmail(user.getEmail());
    existingUser.setStatus(user.getStatus());
    existingUser.setPassword(passwordEncoder.encode(user.getPassword()));
    userRepository.save(existingUser);
    return existingUser;
  }

  public User deleteUser(Long userId) {
    User existingUser = userRepository.findById(userId)
        .orElseThrow(() -> {
          throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid user");
        });

    userRepository.deleteById(userId);

    return existingUser;
  }

  public Optional<User> validateUser(String email, String password) {
    Optional<User> user = userRepository.findByEmail(email);

    if (user.isPresent()) {
        // Si la contraseña es opcional, solo valida si no es nula o vacía
        if (password == null || password.isEmpty()) {
            return user; // Devuelve el usuario si solo se valida el nombre de usuario
        }

        // Si la contraseña no es opcional, valida contra la base de datos
        if (passwordEncoder.matches(password, user.get().getPassword())) {
            return user;
        }
    }

    return Optional.empty(); // Usuario no válido
  }
}
