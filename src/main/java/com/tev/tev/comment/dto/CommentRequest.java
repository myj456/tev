package com.tev.tev.comment.dto;

import com.tev.tev.comment.entity.Comments;

import lombok.Builder;
import lombok.Data;

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
