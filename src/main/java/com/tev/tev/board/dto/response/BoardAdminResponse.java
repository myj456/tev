package com.tev.tev.board.dto.response;

import com.tev.tev.board.entity.Board;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BoardAdminResponse {
    private Integer boardId;
    private String title;
    private String content;
    private String nickname;
    private Long viewCount;
    private Long likeCount;
    private String createdAt;
    private String modifiedAt;

    public static BoardAdminResponse from(Board board){
        return BoardAdminResponse.builder()
                .boardId(board.getBoardId())
                .title(board.getTitle())
                .content(board.getContent())
                .nickname(board.getUser().getNickname())
                .viewCount(board.getViewCount())
                .likeCount(board.getLikeCount())
                .createdAt(board.getCreatedAt())
                .modifiedAt(board.getModifiedAt())
                .build();
    }
}
