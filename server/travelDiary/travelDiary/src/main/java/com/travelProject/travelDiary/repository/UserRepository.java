package com.travelProject.travelDiary.repository;

import com.travelProject.travelDiary.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Long> {

}
