package com.tev.tev.board.dto;

import lombok.Data;
import org.hibernate.annotations.Comment;

import java.time.LocalDateTime;

@Comment("게시글 조회 DTO")
@Data
public class BoardResponse {
    private String title;
    private String comment;
    private LocalDateTime board_createdAt;


}
