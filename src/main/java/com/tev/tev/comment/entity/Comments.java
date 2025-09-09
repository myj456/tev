package com.tev.tev.comment.entity;

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
@Table(name = "comment")
public class Comments {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Comment("댓글 식별자")
    @Column(name = "comment_id")
    private Integer commentId;

    @Comment("댓글 내용")
    @Column(name = "comment_content")
    private String content;

    @Comment("댓글 생성일")
    @Column(name = "comment_created_at")
    private LocalDateTime createdAt;

    @Comment("댓글 수정일")
    @Column(name = "comment_edited_at")
    private LocalDateTime editedAt;

    // CRUD 완료후 매핑할 예정.
    @Comment("게시글 id")
    private Integer boardId;

    // CRUD 완료후 매핑할 예정.
    @Comment("댓글 작성자")
    private Integer userId;
}
