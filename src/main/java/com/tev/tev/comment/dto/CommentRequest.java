package com.tev.tev.comment.dto;

import com.tev.tev.auth.user.entity.User;
import com.tev.tev.board.entity.Board;
import com.tev.tev.comment.entity.Comments;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentRequest {

    @Size(min = 1, max = 200)
    @NotNull(message = "댓글 내용은 필수 값입니다.")
    @NotBlank(message = "댓글은 필수 값입니다.")
    private String content;

    // dto -> entity
    public Comments toEntity(Board board, User user){
        return Comments.builder()
                .content(this.content)
                .createdAt(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .modifiedAt(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .board(board)
                .user(user)
                .build();
    }
}
