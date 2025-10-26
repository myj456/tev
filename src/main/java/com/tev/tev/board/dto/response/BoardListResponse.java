package com.tev.tev.board.dto.response;

import com.tev.tev.board.entity.Board;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BoardListResponse {
    private Integer boardId;
    private String title;
    private String content;
    private String createdAt;
    private String modifiedAt;
    private Long viewCount;
    private Long likeCount;

    public static BoardListResponse from(Board board){
        return BoardListResponse.builder()
                .boardId(board.getBoardId())
                .title(board.getTitle())
                .content(board.getContent())
                .createdAt(board.getCreatedAt())
                .modifiedAt(board.getModifiedAt())
                .viewCount(board.getViewCount())
                .likeCount(board.getLikeCount())
                .build();
    }
}
