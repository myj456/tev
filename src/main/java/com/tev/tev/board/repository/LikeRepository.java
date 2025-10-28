package com.tev.tev.board.repository;

import com.tev.tev.auth.user.entity.User;
import com.tev.tev.board.entity.Board;
import com.tev.tev.board.entity.Likes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LikeRepository extends JpaRepository<Likes, Integer> {
    boolean existsByUserAndBoard(User user, Board board);

    void deleteByUserAndBoard(User user, Board board);
}
