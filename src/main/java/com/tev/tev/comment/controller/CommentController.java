package com.tev.tev.comment.controller;

import com.tev.tev.board.repository.BoardRepository;
import com.tev.tev.comment.dto.CommentRequest;
import com.tev.tev.comment.dto.CommentResponse;
import com.tev.tev.comment.dto.CommentUpdate;
import com.tev.tev.comment.repository.CommentsRepository;
import com.tev.tev.comment.service.CommentService;
import com.tev.tev.common.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/board/{boardid}/comment")
public class CommentController {

    private final CommentService commentService;
    private final CommentsRepository commentsRepository;

    private final BoardRepository boardRepository;

    // 댓글 생성
    @PostMapping
    public ResponseEntity<ApiResponse<String>> commentCreate(@PathVariable("boardid") Integer boardId,
                                                            @Valid @RequestBody CommentRequest commentRequest){
        if(!boardRepository.existsById(boardId)){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.fail("존재하지 않는 게시글입니다. id: " + boardId));
        }

        Integer commentId = commentService.commentSave(boardId, commentRequest);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("댓글 생성 성공 id: " + commentId));
    }

    // 댓글 수정
    @PatchMapping("/{commentid}")
    public ResponseEntity<ApiResponse<CommentResponse>> commentEdit(@PathVariable("boardid") Integer boardId,
                                                                    @PathVariable("commentid") Integer commentId,
                                                                    @Valid @RequestBody CommentUpdate commentUpdate){
        if(!boardRepository.existsById(boardId)){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.fail("존재하지 않는 게시글입니다. id: " + boardId));
        }

        if(!commentsRepository.existsById(commentId)){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.fail("존재하지 않는 댓글입니다. id: " + commentId));
        }

        CommentResponse commentResponse = commentService.commentEdit(boardId, commentId, commentUpdate);
        if(commentResponse == null){
            return ResponseEntity
                    .ok(ApiResponse.success("수정된 사항이 없습니다.", null));
        }

        return ResponseEntity
                .ok(ApiResponse.success("댓글 수정 성공", commentResponse));
    }

    // 댓글 삭제
    @DeleteMapping("/delete")
    public ResponseEntity<ApiResponse<String>> commentDelete(@PathVariable("boardid") Integer boardId,
                                                             @RequestParam("commentid") Integer commentId){
        if(!boardRepository.existsById(boardId)){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.fail("존재하지 않는 게시글입니다. id: " + boardId));
        }

        if(!commentsRepository.existsById(commentId)){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.fail("존재하지 않는 댓글입니다. id: " + commentId));
        }

        commentService.delete(commentId);
        return ResponseEntity
                .ok(ApiResponse.success("댓글 삭제 성공 id: " + commentId));
    }

}
