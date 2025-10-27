package com.tev.tev.comment.dto;

import com.tev.tev.comment.entity.Comments;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentResponse {
    private Integer commentId;
    private String content;
    private String nickname;
    private String createdAt;
    private String modifiedAt;
    private Integer boardId;

    // entity -> dto
    public static CommentResponse from(Comments comments){
        return CommentResponse.builder()
                .commentId(comments.getCommentId())
                .content(comments.getContent())
                .nickname(comments.getUser().getNickname())
                .createdAt(comments.getCreatedAt())
                .modifiedAt(comments.getModifiedAt())
                .boardId(comments.getBoard().getBoardId())
                .build();
    }
}
