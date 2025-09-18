package com.tev.tev.board.dto.request;

import com.tev.tev.board.entity.Board;
import lombok.Builder;
import lombok.Data;

import java.util.Objects;

@Data
@Builder
public class BoardUpdate {
    // TODO: @Valid 적용
    private String title;
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
