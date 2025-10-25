package com.tev.tev.comment.dto;

import com.tev.tev.comment.entity.Comments;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CommentResponse {
    private Integer commentId;
    private String content;
    private String createdAt;
    private String modifiedAt;
    private Integer boardId;

    // entity -> dto
    public static CommentResponse from(Comments comments){
        return CommentResponse.builder()
                .commentId(comments.getCommentId())
                .content(comments.getContent())
                .createdAt(comments.getCreatedAt())
                .modifiedAt(comments.getModifiedAt())
                .boardId(comments.getBoard().getBoardId())
                .build();
    }
}
