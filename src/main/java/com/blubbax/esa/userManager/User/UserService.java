package com.blubbax.esa.userManager.User;

import com.blubbax.esa.userManager.User.entity.User;
import com.blubbax.esa.userManager.User.exception.AuthFailedException;
import com.blubbax.esa.userManager.User.exception.DuplicateUserException;
import com.blubbax.esa.userManager.User.exception.UserNotFoundException;
import com.blubbax.esa.userManager.User.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.UUID;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public List<User> getAllUserDatasets() {
        return userRepository.findAll();
    }

    public User authenticateUser(String username, String password) {
        User queriedUser = userRepository.findByUsername(username);
        if (queriedUser != null && queriedUser.getPassword().equals(password)) {
            return queriedUser;
        }
        throw new AuthFailedException();
    }

    public User getUserDataset(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
    }

    public User saveUserDataset(User user) {
        if (!userRepository.existsByUsername(user.getUsername())){
            return userRepository.save(user);
        } else {
            throw new DuplicateUserException(user.getUsername());
        }
    }

    public User updateUserDataset(long id, User newUser) {
        return userRepository.findById(id)
                .map(user -> {
                    if (!user.getUsername().equals(newUser.getUsername()) && userRepository.findByUsername(newUser.getUsername()) == null) {
                        user.setUsername(newUser.getUsername());
                        user.setPassword(newUser.getPassword());
                        return userRepository.save(user);
                    } else {
                        throw new DuplicateUserException(newUser.getUsername());
                    }
                }).orElseGet(() -> {
                    if (!userRepository.existsByUsername(newUser.getUsername())) {
                        newUser.setId(id);
                        return userRepository.save(newUser);
                    } else {
                        throw new DuplicateUserException(newUser.getUsername());
                    }
                });
    }

    public void deleteUserDataset(Long id) throws EmptyResultDataAccessException {
        userRepository.deleteById(id);
    }

}
