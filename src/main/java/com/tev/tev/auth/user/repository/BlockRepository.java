package com.tev.tev.auth.user.repository;

import com.tev.tev.auth.user.entity.Block;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BlockRepository extends JpaRepository<Block, Integer> {

    boolean existsByBlockedUserId(Integer blockedUserId);

    Block findByBlockedUserId(Integer blockedUserId);
}