package com.example.taskforwork.serviceImpl;

import com.example.taskforwork.entities.Email;
import com.example.taskforwork.entities.PhoneNumber;
import com.example.taskforwork.entities.User;
import com.example.taskforwork.repositories.UserRepository;
import com.example.taskforwork.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(PasswordEncoder passwordEncoder, UserRepository userRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    // Register a new user
    @Override
    public User registerUser(User user) {
        if (userRepository.findUserByUsername(user.getUsername()) != null) {
            throw new RuntimeException("User Exists");
        }
        String hashedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(hashedPassword);
        return userRepository.save(user);
    }

    // Get user by ID
    @Override
    public Optional<User> getUserById(Long id) {
        Optional<User> user = userRepository.findUserById(id);
        if (!user.isPresent()) {
            throw new RuntimeException("There is no such a user with id: " + id);
        }
        return user;
    }

    // Get user information by login
    @Override
    public User getUserByLogin(String login) {
        return this.findByUsername(login);
    }

    // Add phone number by user ID
    @Override
    public User addPhoneNumber(Long userId, PhoneNumber phoneNumber) {
        User user = userRepository.findById(userId).orElse(null);
        if (user != null) {
            user.addPhoneNumber(phoneNumber);
            return userRepository.save(user);
        }
        return null;
    }

    // Add email by user ID
    @Override
    public User addEmail(Long userId, Email email) {
        User user = userRepository.findById(userId).orElse(null);
        if (user != null) {
            user.addEmail(email);
            return userRepository.save(user);
        }
        return null;
    }

    // Get a list of users with pagination
    @Override
    public Page<User> getAllUsers(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        return userRepository.findAll(pageRequest);
    }

    // Update user by ID
    @Override
    public User updateUser(Long userId, User updatedUser) {
        User user = userRepository.findById(userId).orElse(null);
        if (user != null) {
            user.setFirstName(updatedUser.getFirstName());
            user.setLastName(updatedUser.getLastName());
            user.setAge(updatedUser.getAge());
            user.setPassword(updatedUser.getPassword());
            return userRepository.save(user);
        }
        return null;
    }

    // Update user's email by ID
    @Override
    public User updateUserEmailById(Long userId, Email updatedEmail) {
        User user = userRepository.findById(userId).orElse(null);
        if (user != null) {
            Email emailToUpdate = user.getEmails().stream()
                    .filter(email -> email.getId().equals(updatedEmail.getId()))
                    .findFirst()
                    .orElse(null);

            if (emailToUpdate != null) {
                emailToUpdate.setEmail(updatedEmail.getEmail());
                return userRepository.save(user);
            }
        }
        return null;
    }

    // Update user's phone number by ID
    @Override
    public User updateUserPhoneNumberById(Long userId, PhoneNumber updatedPhoneNumber) {
        User user = userRepository.findById(userId).orElse(null);
        if (user != null) {
            PhoneNumber phoneNumberToUpdate = user.getPhoneNumbers().stream()
                    .filter(phoneNumber -> phoneNumber.getId().equals(updatedPhoneNumber.getId()))
                    .findFirst()
                    .orElse(null);

            if (phoneNumberToUpdate != null) {
                phoneNumberToUpdate.setPhoneNumber(updatedPhoneNumber.getPhoneNumber());
                phoneNumberToUpdate.setCountry(updatedPhoneNumber.getCountry());
                phoneNumberToUpdate.setLabel(updatedPhoneNumber.getLabel());
                return userRepository.save(user);
            }
        }
        return null;
    }

    // Delete user by ID
    @Override
    public boolean deleteUserById(Long userId) {
        if (userRepository.existsById(userId)) {
            userRepository.deleteById(userId);
            return true;
        }
        return false;
    }


    @Override
    public User findByUsername(String username) {
        User dbUser = userRepository.findUserByUsername(username);
        if (dbUser == null) {
            throw new RuntimeException("There is no such a user");
        }
        return dbUser;
    }

}
