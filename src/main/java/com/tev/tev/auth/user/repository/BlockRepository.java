package com.tev.tev.auth.user.repository;

import com.tev.tev.auth.user.entity.Block;
import com.tev.tev.auth.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BlockRepository extends JpaRepository<Block, Integer> {

    boolean existsByUser(User user);

    Block findByUser(User user);
}