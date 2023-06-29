package com.neymeha.socialmediasecurityapi.repository;

import com.neymeha.socialmediasecurityapi.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

// репозиторий для работы с БД для КРУД операций с пользователем
public interface UserRepository extends JpaRepository <User, Long> {
    Optional <User> findByEmail(String email);
}
