package com.blubbax.esa.userManager.User;

import com.blubbax.esa.userManager.User.exception.UserNotFoundException;
import com.blubbax.esa.userManager.User.entity.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "Get current server status")
    @GetMapping("/api")
    public String getInfo() {
        return "Server is up and running";
    }

    @Operation(summary = "Get all users")
    @GetMapping("/api/user")
    public List<User> getAllUserDatasets() {
        return userService.getAllUserDatasets();
    }

    @Operation(summary = "Authenticate user")
    @PostMapping("/api/auth")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Authentication successful",
                    content = @Content),
            @ApiResponse(responseCode = "401", description = "Authentication failed",
                    content = @Content)
    })
    public User authenticateUser(@RequestBody User user) {
        return userService.authenticateUser(user.getUsername(), user.getPassword());
    }

    @Operation(summary = "Get user by its id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User found",
                    content = { @Content(mediaType = "application/json", schema = @Schema(implementation = User.class))}),
            @ApiResponse(responseCode = "404", description = "No user found",
                    content = @Content)
    })
    @GetMapping("/api/user/{id}")
    public User getUserDataset(@PathVariable Long id) {
        return userService.getUserDataset(id);
    }

    @Operation(summary = "Save new user entry")
    @PostMapping("/api/user")
    public User saveUserDataset(@RequestBody User user) {
        return userService.saveUserDataset(user);
    }

    @Operation(summary = "Update user by its id")
    @PutMapping("/api/user/{id}")
    public User updateUserDataset(@PathVariable Long id, @RequestBody User newUser) {
        return userService.updateUserDataset(id, newUser);
    }

    @Operation(summary = "Delete user by its id")
    @DeleteMapping("/api/user/{id}")
    public void deleteUserDataset(@PathVariable Long id) {
        try {
            userService.deleteUserDataset(id);
        } catch (EmptyResultDataAccessException ex) {
            throw new UserNotFoundException(id);
        }
    }

}
