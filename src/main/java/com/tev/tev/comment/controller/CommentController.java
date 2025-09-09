package com.tev.tev.comment.controller;

import com.tev.tev.comment.dto.CommentCreate;
import com.tev.tev.comment.dto.CommentEdit;
import com.tev.tev.comment.entity.Comments;
import com.tev.tev.comment.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/board/{boardid}/comment")
public class CommentController {

    private final CommentService commentService;

    // 댓글 생성
    // 유저 값은 Security를 통해 가져오기.
    @PostMapping()
    public String createComment(@PathVariable("boardid") Integer boardId,
                                CommentCreate commentCreate){
        commentService.create(boardId, commentCreate);
        return "댓글 생성 완료";
    }

    @GetMapping("/view/{commentid}")
    public Comments viewComment(@PathVariable("boardid") Integer boardId,
                              @PathVariable("commentid") Integer commentId){
        return commentService.view(commentId);
    }

    // 댓글 수정
    @PatchMapping("/edit")
    public String editComment(@PathVariable("boardid") Integer boardId,
                              @RequestParam("commentid") Integer commentId,
                              CommentEdit commentEdit){
        commentService.edit(commentId, commentEdit);
        return "댓글 수정 완료 id: " + commentId;
    }

    // 댓글 삭제
    @DeleteMapping("/delete")
    public String deleteComment(@PathVariable("boardid") Integer boardId,
                                @RequestParam("commentid") Integer commentId){
        commentService.delete(commentId);
        return "댓글 삭제 완료 id: " + commentId;
    }

}
