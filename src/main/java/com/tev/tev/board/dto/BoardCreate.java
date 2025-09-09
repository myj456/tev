package com.tev.tev.board.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;


@Comment("게시글 생성 DTO")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BoardCreate {
    private String title;
    private String content;
    private Integer userId;
}
