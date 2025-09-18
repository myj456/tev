package com.tev.tev.board.dto.response;

import com.tev.tev.board.entity.Board;
import com.tev.tev.comment.dto.CommentResponse;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class BoardResponse {
    private Integer boardId;
    private String title;
    private String content;
    private Long viewCount;
    private Long likeCount;
    private String createdAt;
    private String modifiedAt;

    private List<CommentResponse> commentList;

    // entity -> dto
    public static BoardResponse from(Board board){
        return BoardResponse.builder()
                .boardId(board.getBoardId())
                .title(board.getTitle())
                .content(board.getContent())
                .viewCount(board.getViewCount())
                .likeCount(board.getLikeCount())
                .createdAt(board.getCreatedAt())
                .modifiedAt(board.getModifiedAt())
                .commentList(board.getCommentsList().stream()
                        .map(CommentResponse::from)
                        .toList())
                .build();
    }


}
