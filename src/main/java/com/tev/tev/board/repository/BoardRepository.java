package com.tev.tev.board.repository;

import com.tev.tev.board.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BoardRepository extends JpaRepository<Board, Integer> {
    Board findByBoardId(Integer boardId);

    List<Board> findByUserId(Integer userId);
}
