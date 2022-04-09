package com.blubbax.esa.userManager.User;

import com.blubbax.esa.userManager.User.entity.User;
import com.blubbax.esa.userManager.User.exception.AuthFailedException;
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

    public User saveUserDataset(@RequestBody User transaction) {
        return userRepository.save(transaction);
    }

    public User updateUserDataset(@PathVariable long id, @RequestBody User newUser) {
        return userRepository.findById(id)
                .map(user -> {
                    user.setUsername(newUser.getUsername());
                    user.setPassword(newUser.getPassword());
                    return userRepository.save(user);
                }).orElseGet(() -> {
                    newUser.setId(id);
                    return userRepository.save(newUser);
                });
    }

    public void deleteUserDataset(@PathVariable Long id) throws EmptyResultDataAccessException {
        userRepository.deleteById(id);
    }

}
