package com.workflow.service;

import com.workflow.entity.User;
import com.workflow.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepo userRepo;

    public List<User> getAllUsers() {
        return userRepo.findAll();
    }

    public Optional<User> getUserById(long userId) {
        return userRepo.findById(userId);
    }

    public User addNewUser(User addUser) {
        return userRepo.save(addUser);
    }

    public Optional<User> updateUser(long userId, User updateUser) {
        Optional<User> optionalUser = userRepo.findById(userId);

        if (optionalUser.isPresent()) {
            User existingUser = optionalUser.get();
            existingUser.setUserName(updateUser.getUserName());
            existingUser.setUserEmail(updateUser.getUserEmail());
            userRepo.save(existingUser);
            return Optional.of(existingUser);
        } else {
            return Optional.empty();
        }
    }

    public boolean deleteUserById(long id) {
        if (userRepo.existsById(id)) {
            userRepo.deleteById(id);
            return true;
        }
        return false;
    }
}