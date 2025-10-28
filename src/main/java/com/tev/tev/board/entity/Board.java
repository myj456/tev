package com.tev.tev.board.entity;

import com.tev.tev.auth.user.entity.User;
import com.tev.tev.comment.entity.Comments;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Comment;

import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "board")
@Builder
public class Board {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Comment("게시글 식별자")
    @Column(name = "board_id")
    private Integer boardId;

    @Comment("게시글 제목")
    @Column(name = "board_title")
    private String title;

    // 차후에 마크업으로 변경할 예정
    @Comment("게시글 내용")
    @Column(name = "board_content", columnDefinition = "TEXT")
    private String content;

    @Comment("게시글 생성일")
    @Column(name = "board_created_at")
    private String createdAt;

    @Comment("게시글 수정일")
    @Column(name = "board_modified_at")
    private String modifiedAt;

    @Comment("게시글 조회수")
    @Column(name = "board_view_count")
    @ColumnDefault("0")
    @Builder.Default
    private Long viewCount = 0L;

    @Comment("게시글 좋아요수")
    @Column(name = "board_like_count")
    @ColumnDefault("0")
    @Builder.Default
    private Long likeCount = 0L;

    @OneToMany(mappedBy = "board", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Likes> likes;

    @Comment("댓글 리스트")
    @OneToMany(mappedBy = "board", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Comments> commentsList;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    // 조회수 증가
    public void upViewCount(){
        this.viewCount++;
    }

    // 좋아요 증가
    public void upLikeCount(){
        this.likeCount++;
    }

    // 좋아요 감소
    public void downLikeCount(){
        if(likeCount > 0){
            this.likeCount--;
        }
    }
}
