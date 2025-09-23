package com.tev.tev.comment.repository;

import com.tev.tev.comment.entity.Comments;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentsRepository extends JpaRepository<Comments, Integer> {

    // cursorId가 null일 때
    List<Comments> findAllByOrderByCommentIdDescCreatedAtDesc(Pageable pageable);

    // cursorId가 null이 아닐 때
    List<Comments> findByCommentIdLessThanOrderByCommentIdDescCreatedAtDesc(@Param("id") long id, Pageable pageable);
}
