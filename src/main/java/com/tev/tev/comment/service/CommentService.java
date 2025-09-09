package com.tev.tev.comment.service;

import com.tev.tev.comment.dto.CommentCreate;
import com.tev.tev.comment.dto.CommentEdit;
import com.tev.tev.comment.entity.Comments;
import com.tev.tev.comment.repository.CommentsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentsRepository commentsRepository;

    // 댓글 생성
    public void create(Integer boardId, CommentCreate commentCreate){
        Comments comments = new Comments();
        comments.setContent(commentCreate.getContent());
        comments.setBoardId(boardId);
        comments.setUserId(commentCreate.getUserId());
        comments.setCreatedAt(LocalDateTime.now());
        commentsRepository.save(comments);
    }

    // 댓글 id 조회
    public Comments view(Integer commentId){
        return commentsRepository.findByCommentId(commentId);
    }

    // 댓글 수정
    public void edit(Integer commentId, CommentEdit commentEdit){
        Comments comments = commentsRepository.findByCommentId(commentId);

        comments.setContent(commentEdit.getContent());
        comments.setEditedAt(LocalDateTime.now());
        commentsRepository.save(comments);
    }

    // 댓글 삭제
    public void delete(Integer commentId){
        commentsRepository.deleteById(commentId);
    }
}
