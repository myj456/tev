package com.tev.tev.board.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "board")
public class Board {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Comment("게시글 식별자")
    @Column(name = "board_id")
    private Integer boardId;

    @Comment("게시글 제목")
    @Column(name = "board_title")
    private String title;

    @Comment("게시글 내용")
    @Column(name = "board_content")
    private String content;

    @Comment("게시글 생성일")
    @Column(name = "board_created_at")
    private LocalDateTime board_createdAt;

    @Comment("게시글 수정일")
    @Column(name = "board_edited_at")
    private LocalDateTime board_editedAt;

    // CRUD 완료후 매핑할 예정.
    @Comment("유저 아이디")
    private Integer userId;
}
