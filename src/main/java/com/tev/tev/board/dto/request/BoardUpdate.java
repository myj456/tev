package com.tev.tev.board.dto.request;

import com.tev.tev.board.entity.Board;
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
public class BoardUpdate {
    @Size(min = 0, max = 50)
    private String title;

    @Size(min = 0, max = 2000)
    private String content;

    public boolean update(Board board){
        boolean isUpdated = false;
        if(!Objects.equals(this.title, board.getTitle())){
            board.setTitle(this.title);
            isUpdated = true;
        }
        if(!Objects.equals(this.content, board.getContent())){
            board.setContent(this.content);
            isUpdated = true;
        }

        return isUpdated;
    }
}
