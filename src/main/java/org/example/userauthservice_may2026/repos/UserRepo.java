package org.example.userauthservice_may2026.repos;

import org.example.userauthservice_may2026.models.Role;
import org.example.userauthservice_may2026.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepo extends JpaRepository<User,Long> {
    Optional<User> findByEmail(String email);

}
