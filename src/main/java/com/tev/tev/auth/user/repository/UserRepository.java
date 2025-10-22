package com.tev.tev.auth.user.repository;

import com.tev.tev.auth.user.entity.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    boolean existsByNickname(String nickname);

    boolean existsByEmail(String email);

    Optional<User> findByEmail(String email);

    List<User> findAllByOrderByUserIdDescCreatedAtDesc(Pageable pageable);

    List<User> findByNicknameContainingOrEmailContainingOrderByUserIdDescCreatedAtDesc(String nickname, String email, Pageable pageable);
}
