package com.tev.tev.board.dto.request;

import com.tev.tev.auth.user.entity.User;
import com.tev.tev.board.entity.Board;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BoardRequest {

    @Max(50)
    @NotNull(message = "제목은 필수 값입니다.")
    @NotBlank(message = "제목은 필수 값입니다.")
    private String title;

    @Max(1000)
    @NotNull(message = "게시글 내용은 필수 값입니다.")
    @NotBlank(message = "게시글 내용은 필수 값입니다.")
    private String content;

    // dto -> entity
    public Board toEntity(User user){
        return Board.builder()
                .title(this.title)
                .content(this.content)
                .createdAt(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .modifiedAt(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .user(user)
                .build();
    }
}
