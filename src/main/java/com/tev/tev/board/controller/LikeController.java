package com.tev.tev.board.controller;

import com.tev.tev.board.dto.response.LikeResponse;
import com.tev.tev.board.service.LikeService;
import com.tev.tev.common.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/board")
public class LikeController {

    private final LikeService likeService;

    @PostMapping("/{boardid}")
    public ResponseEntity<ApiResponse<LikeResponse>> likeBoard(@PathVariable("boardid") Integer boardId){
        LikeResponse likeResponse = likeService.likeBoard(boardId);
        return ResponseEntity
                .ok(ApiResponse.success(likeResponse));
    }
}
