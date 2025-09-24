package com.tev.tev.auth.common.repository;

import com.tev.tev.auth.common.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    boolean existsByNickname(String nickname);

    boolean existsByEmail(String email);

    User findByEmail(String email);
}
