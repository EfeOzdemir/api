package com.plantapp.api.core.repository;


import com.plantapp.api.core.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {
}
