package com.tev.tev.auth.user.dto;

import com.tev.tev.auth.user.entity.User;
import com.tev.tev.board.dto.response.BoardAdminResponse;
import com.tev.tev.comment.dto.CommentResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDetailResponse {
    private Integer id;
    private String nickname;
    private String email;
    private String role;
    private String createdAt;
    private List<BoardAdminResponse> boardList;
    private List<CommentResponse> commentList;

    // entity -> dto
    public static UserDetailResponse from(User user){
        return UserDetailResponse.builder()
                .id(user.getUserId())
                .nickname(user.getNickname())
                .email(user.getEmail())
                .role(user.getRole().name())
                .createdAt(user.getCreatedAt())
                .boardList(user.getBoardList().stream()
                        .map(BoardAdminResponse::from)
                        .toList())
                .commentList(user.getCommentList().stream()
                        .map(CommentResponse::from)
                        .toList())
                .build();
    }
}
