package com.project.lms.service;

import com.project.lms.entity.User;
import com.project.lms.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;


    public UserService(UserRepository userRepository) {

        this.userRepository = userRepository;
    }


    public List<User> getAllUsers() {

        return userRepository.findAll();
    }

    public User getUserById(Long id) {

        return userRepository.findById(id)
                .orElseThrow(
                        () -> new RuntimeException(
                                "User not found"
                        )
                );
    }

    public User getUserByEmail(String email) {

        return userRepository.findByEmail(email)
                .orElseThrow(
                        () -> new RuntimeException(
                                "User not found"
                        )
                );
    }

    public void deleteUser(Long id) {

        if (!userRepository.existsById(id)) {

            throw new RuntimeException(
                    "User not found"
            );
        }

        userRepository.deleteById(id);
    }
}