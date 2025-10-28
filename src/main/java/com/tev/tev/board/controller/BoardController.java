package com.tev.tev.board.controller;

import com.tev.tev.board.dto.request.BoardRequest;
import com.tev.tev.board.dto.response.BoardListResponse;
import com.tev.tev.board.dto.response.BoardResponse;
import com.tev.tev.board.dto.request.BoardUpdate;
import com.tev.tev.board.repository.BoardRepository;
import com.tev.tev.board.service.BoardService;
import com.tev.tev.common.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/board")
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;
    private final BoardRepository boardRepository;
    // 게시글 생성
    @PostMapping("/create")
    public ResponseEntity<ApiResponse<String>> boardCreate(@Valid @RequestBody BoardRequest boardRequest){
        Integer boardId = boardService.saveBoard(boardRequest);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse
                        .success("게시글 생성 성공 id:" + boardId));
    }

    // 게시글 검색 (title)
    @GetMapping("/list")
    public ResponseEntity<ApiResponse<List<BoardListResponse>>> searchBoardList(@RequestParam(value = "search", required = false) String keyword,
                                                                                @RequestParam(value = "page", defaultValue = "0") int page,
                                                                                @RequestParam(value = "size", defaultValue = "10") int size){
        List<BoardListResponse> boardListResponses = boardService.searchBoardList(keyword, page, size);
        return ResponseEntity
                .ok(ApiResponse.success(boardListResponses));

    }

    // 게시글 조회 - id
    @GetMapping("/{boardid}")
    public ResponseEntity<ApiResponse<BoardResponse>> viewBoard(@PathVariable("boardid") Integer boardId,
                                                                @RequestParam(value = "cursorId", required = false) Long cursorId,
                                                                @RequestParam(value = "pageSize", defaultValue = "15") int pageSize){
        if(!boardRepository.existsById(boardId)){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.fail("존재하지 않는 게시글입니다. id: " + boardId));
        }

        BoardResponse boardResponse = boardService.getBoardById(boardId, cursorId, pageSize);
        return ResponseEntity
                .ok(ApiResponse.success("게시글 조회 성공", boardResponse));
    }

    // 게시글 수정
    @PatchMapping("/{boardid}/edit")
    public ResponseEntity<ApiResponse<BoardResponse>> editBoard(@PathVariable("boardid") Integer boardId,
                                                                @Valid @RequestBody BoardUpdate boardUpdate){


        if(!boardRepository.existsById(boardId)){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.fail("존재하지 않는 게시글입니다. id: " + boardId));
        }

        BoardResponse boardResponse = boardService.updateBoard(boardId, boardUpdate);
        if(boardResponse == null){
            BoardResponse findBoard = boardService.getBoardById(boardId, null, 0);
            return ResponseEntity
                    .ok(ApiResponse.success("수정된 사항이 없습니다.", findBoard));
        }

        return ResponseEntity
                .ok(ApiResponse.success("게시글 수정 성공", boardResponse));
    }

    @DeleteMapping("/{boardid}/delete")
    public ResponseEntity<ApiResponse<String>> deleteBoard(@PathVariable("boardid") Integer boardId){
        if(!boardRepository.existsById(boardId)){
            return ResponseEntity.badRequest()
                    .body(ApiResponse.fail("존재하지 않는 게시글입니다. id: " + boardId));
        }

        boardService.delete(boardId);
        return ResponseEntity.ok(ApiResponse.success("게시글 삭제 성공 id: " + boardId));
    }
}
