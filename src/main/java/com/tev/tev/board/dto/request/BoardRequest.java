package com.tev.tev.board.dto.request;

import com.tev.tev.board.entity.Board;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BoardRequest {
    //TODO: @Valid 적용

    private String title;
    private String content;

    // dto -> entity
    public Board toEntity(){
        return Board.builder()
                .title(this.title)
                .content(this.content)
                .build();
    }
}
