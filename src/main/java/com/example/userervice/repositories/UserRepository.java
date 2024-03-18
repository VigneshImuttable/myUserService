package com.example.userervice.repositories;

import com.example.userervice.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface
UserRepository extends JpaRepository<User,Long> {

    User save (User user);
    Optional<User> findFirstByEmail(String email);
}
