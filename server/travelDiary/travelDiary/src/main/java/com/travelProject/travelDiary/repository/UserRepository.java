package com.travelProject.travelDiary.repository;

import com.travelProject.travelDiary.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, String> {

    public Optional<User> findByEmail (String email);

}
