package com.tev.tev.comment.dto;

import com.tev.tev.board.entity.Board;
import com.tev.tev.comment.entity.Comments;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
@Builder
public class CommentRequest {
    private String content;

    // dto -> entity
    public Comments toEntity(){
        return Comments.builder()
                .content(this.content)
                .build();
    }
}
