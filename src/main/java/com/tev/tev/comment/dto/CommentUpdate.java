package com.tev.tev.comment.dto;

import com.tev.tev.comment.entity.Comments;
import lombok.Builder;
import lombok.Data;

import java.util.Objects;

@Data
@Builder
public class CommentUpdate {
    private String content;

    public boolean update(Comments comments){
        boolean isUpdated = false;

        if(!Objects.equals(this.content, comments.getContent())){
            comments.setContent(content);
            isUpdated = true;
        }

        return isUpdated;
    }
}
