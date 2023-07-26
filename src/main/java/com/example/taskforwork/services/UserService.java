package com.example.taskforwork.services;

import com.example.taskforwork.entities.Email;
import com.example.taskforwork.entities.PhoneNumber;
import com.example.taskforwork.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;
import java.util.Optional;

public interface UserService {
    User registerUser(User user);

    Optional<User> getUserById(Long id);

    User getUserByLogin(String login);

    User addPhoneNumber(Long userId, PhoneNumber phoneNumber);

    User addEmail(Long userId, Email email);

    Page<User> getAllUsers(int page, int size);

    User updateUser(Long userId, User updatedUser);

    User updateUserEmailById(Long userId, Email updatedEmail);

    User updateUserPhoneNumberById(Long userId, PhoneNumber updatedPhoneNumber);

    boolean deleteUserById(Long userId);

    User findByUsername(String username);
}
