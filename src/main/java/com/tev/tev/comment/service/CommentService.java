package com.tev.tev.comment.service;

import com.tev.tev.board.entity.Board;
import com.tev.tev.board.repository.BoardRepository;
import com.tev.tev.comment.dto.CommentRequest;
import com.tev.tev.comment.dto.CommentResponse;
import com.tev.tev.comment.dto.CommentUpdate;
import com.tev.tev.comment.entity.Comments;
import com.tev.tev.comment.repository.CommentsRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.xml.stream.events.Comment;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

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

    // 댓글 전체 조회
    public List<CommentResponse> commentGetList(Long cursorId, int pageSize){
        Pageable pageable = PageRequest.of(0, pageSize+1);
        List<Comments> comments = findAllByCursorIdCheckExistsCursor(cursorId, pageable);

        // 댓글 존재 확인
        boolean hasNext = comments.size() > pageSize;

        if(hasNext){
            comments = comments.subList(0, pageSize);
        }
        return toSubListIfHasNext(hasNext, pageSize, comments).stream()
                .map(CommentResponse::from)
                .collect(Collectors.toList());
    }

    // cursorId null 여부
    private List<Comments> findAllByCursorIdCheckExistsCursor(Long cursorId, Pageable pageable){
        return cursorId == null ? commentsRepository.findAllByOrderByCommentIdDescCreatedAtDesc(pageable)
                : commentsRepository.findByCommentIdLessThanOrderByCommentIdDescCreatedAtDesc(cursorId, pageable);
    }

    private List<Comments> toSubListIfHasNext(boolean hasNext, int pageSize, List<Comments> comments){
        return hasNext ? comments.subList(0, pageSize) : comments;
    }



    // 댓글 수정
    public CommentResponse commentEdit(Integer boardId, Integer commentId, CommentUpdate commentUpdate){
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
