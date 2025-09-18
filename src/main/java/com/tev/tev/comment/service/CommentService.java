package com.tev.tev.comment.service;

import com.tev.tev.board.entity.Board;
import com.tev.tev.board.repository.BoardRepository;
import com.tev.tev.comment.dto.CommentRequest;
import com.tev.tev.comment.dto.CommentResponse;
import com.tev.tev.comment.dto.CommentUpdate;
import com.tev.tev.comment.entity.Comments;
import com.tev.tev.comment.repository.CommentsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.xml.stream.events.Comment;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentsRepository commentsRepository;
    private final BoardRepository boardRepository;

    // 댓글 생성
    public Integer commentSave(Integer boardId, CommentRequest commentRequest){
        Board board = boardRepository.findById(boardId).orElseThrow(() ->
                new IllegalArgumentException("존재하지 않는 게시글입니다. id: " + boardId));

        Comments comments = Comments.builder()
                .content(commentRequest.getContent())
                .createdAt(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .modifiedAt(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .board(board)
                .build();

        commentsRepository.save(comments);
        return comments.getCommentId();
    }

    // 댓글 수정
    public CommentResponse CommentEdit(Integer boardId, Integer commentId, CommentUpdate commentUpdate){
        Board board = boardRepository.findById(boardId).orElseThrow(() ->
                new IllegalArgumentException("존재하지 않는 게시글입니다. id: " + boardId));

        Comments comments = commentsRepository.findById(commentId).orElseThrow(() ->
                new IllegalArgumentException("존재하지 않는 댓글입니다. id: " + commentId));

        if(commentUpdate.update(comments)){
           comments.setModifiedAt(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
           commentsRepository.save(comments);
           return CommentResponse.from(comments);
        }

        //TODO: 커스텀 예외처리로 변경w
        return null;
    }

    // 댓글 삭제
    public void delete(Integer commentId){
        Comments comments = commentsRepository.findById(commentId).orElseThrow(() ->
                new IllegalArgumentException("존재하지 않는 댓글입니다. id: " + commentId));

        commentsRepository.deleteById(commentId);
    }
}
