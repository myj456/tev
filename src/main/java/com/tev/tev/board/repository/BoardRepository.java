package com.tev.tev.board.repository;

import com.tev.tev.board.entity.Board;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BoardRepository extends JpaRepository<Board, Integer> {

    // 페이지네이션(offset) id -> createdAt DESC
    List<Board> findAllByOrderByBoardIdDescCreatedAtDesc(Pageable pageable);

    // 검색
    List<Board> findByTitleContainingOrderByBoardIdDescCreatedAtDesc(String keyword, Pageable pageable);
}
