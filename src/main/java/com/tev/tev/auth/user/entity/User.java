package com.tev.tev.auth.user.entity;

import com.tev.tev.board.entity.Board;
import com.tev.tev.comment.entity.Comments;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "user")
@Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Comment("유저 식별자")
    @Column(name = "user_id")
    private Integer userId;

    @Comment("유저 이메일")
    @Column(name = "email")
    private String email;

    @Comment("유저 비밀번호")
    @Column(name = "user_pw")
    private String password;

    @Comment("유저 닉네임")
    @Column(name = "nick_name")
    private String nickname;

    @Comment("유저 생성일")
    @Column(name = "created_at")
    private String createdAt;

    @Comment("유저 권한")
    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private Roles role;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Board> boardList;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comments> commentList;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Block block;
}
