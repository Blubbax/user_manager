package com.blubbax.esa.userManager.User.repository;

import com.blubbax.esa.userManager.User.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
}
