package com.tev.tev.comment.dto;

import com.tev.tev.comment.entity.Comments;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentUpdate {
    @Size(min = 0, max = 200)
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
