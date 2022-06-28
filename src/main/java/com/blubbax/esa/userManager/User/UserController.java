package com.blubbax.esa.userManager.User;

import com.blubbax.esa.userManager.User.exception.DuplicateUserException;
import com.blubbax.esa.userManager.User.exception.UserNotFoundException;
import com.blubbax.esa.userManager.User.entity.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.BasicLinkBuilder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "Get root system info")
    @GetMapping("/")
    public String getRoot() {
        return "User Manager API";
    }

    @Operation(summary = "Get current server status")
    @GetMapping("/api")
    public String getInfo() {
        return "Server is up and running";
    }

    @Operation(summary = "Get all users")
    @GetMapping(value = "/api/user", produces = { "application/hal+json" })
    public List<User> getAllUserDatasets() {
        List<User> users = userService.getAllUserDatasets();
        for (User user: users) {
            user.add(Link.of(linkTo(methodOn(UserController.class).getUserDataset(user.getId())).toString()
                    .replace(BasicLinkBuilder.linkToCurrentMapping().toString(), ""), "self"));
        }
        return users;
    }

    @Operation(summary = "Authenticate user")
    @PostMapping("/api/auth")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Authentication successful",
                    content = { @Content(mediaType = "application/hal+json", schema = @Schema(implementation = User.class))}),
            @ApiResponse(responseCode = "400", description = "User not valid",
                    content = @Content),
            @ApiResponse(responseCode = "401", description = "Authentication failed",
                    content = @Content)
    })
    public User authenticateUser(@RequestBody User user) {
        User auth_user = userService.authenticateUser(user.getUsername(), user.getPassword());
        auth_user.add(Link.of(linkTo(methodOn(UserController.class).getUserDataset(auth_user.getId())).toString()
                .replace(BasicLinkBuilder.linkToCurrentMapping().toString(), ""), "self"));
        return auth_user;
    }

    @Operation(summary = "Get user by its id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User found",
                    content = { @Content(mediaType = "application/hal+json", schema = @Schema(implementation = User.class))}),
            @ApiResponse(responseCode = "404", description = "No user found",
                    content = @Content)
    })
    @GetMapping("/api/user/{id}")
    public User getUserDataset(@PathVariable Long id) {
        User user = userService.getUserDataset(id);

        user.add(Link.of(linkTo(methodOn(UserController.class).getUserDataset(id)).toString()
                .replace(BasicLinkBuilder.linkToCurrentMapping().toString(), ""), "self"));

        return user;
    }

    @Operation(summary = "Save new user entry")
    @PostMapping("/api/user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User successfully added",
                    content = { @Content(mediaType = "application/hal+json", schema = @Schema(implementation = User.class))}),
            @ApiResponse(responseCode = "400", description = "User not valid",
                    content = @Content)
    })
    public User saveUserDataset(@RequestBody User user) {
        User new_user = userService.saveUserDataset(user);

        new_user.add(Link.of(linkTo(methodOn(UserController.class).getUserDataset(new_user.getId())).toString()
                .replace(BasicLinkBuilder.linkToCurrentMapping().toString(), ""), "self"));

        return new_user;
    }

    @Operation(summary = "Update user by its id")
    @PutMapping("/api/user/{id}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User successfully updated",
                    content = { @Content(mediaType = "application/hal+json", schema = @Schema(implementation = User.class))}),
            @ApiResponse(responseCode = "400", description = "User not valid",
                    content = @Content)
    })
    public User updateUserDataset(@PathVariable Long id, @RequestBody User newUser) {
        User updated_user = userService.updateUserDataset(id, newUser);

        updated_user.add(Link.of(linkTo(methodOn(UserController.class).getUserDataset(updated_user.getId())).toString()
                .replace(BasicLinkBuilder.linkToCurrentMapping().toString(), ""), "self"));

        return updated_user;
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
