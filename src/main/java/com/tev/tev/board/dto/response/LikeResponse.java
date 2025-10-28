package com.tev.tev.board.dto.response;

import com.tev.tev.board.entity.Likes;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LikeResponse {
    private Integer boardId;
    private Long likeCount;
    private Integer userId;

    public static LikeResponse from(Likes likes){
        return LikeResponse.builder()
                .boardId(likes.getBoard().getBoardId())
                .likeCount(likes.getBoard().getLikeCount())
                .userId(likes.getUser().getUserId())
                .build();
    }


}
