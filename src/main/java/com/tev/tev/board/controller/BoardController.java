package com.tev.tev.board.controller;

import com.tev.tev.board.dto.request.BoardRequest;
import com.tev.tev.board.dto.response.BoardListResponse;
import com.tev.tev.board.dto.response.BoardResponse;
import com.tev.tev.board.dto.request.BoardUpdate;
import com.tev.tev.board.entity.Board;
import com.tev.tev.board.repository.BoardRepository;
import com.tev.tev.board.service.BoardService;
import com.tev.tev.comment.dto.CommentResponse;
import com.tev.tev.comment.service.CommentService;
import com.tev.tev.common.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.hibernate.query.Page;
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

    private final CommentService commentService;

    // 게시글 생성
    @PostMapping("/create")
    public ResponseEntity<ApiResponse<String>> boardCreate(BoardRequest boardRequest){
        Integer boardId = boardService.saveBoard(boardRequest);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse
                        .success("게시글 생성 성공 id:" + boardId));
    }

    // 게시글 모두 조회
    @GetMapping("/list")
    public ResponseEntity<ApiResponse<List<BoardListResponse>>> boardList(@RequestParam(defaultValue = "0") int page,
                                                                          @RequestParam(defaultValue = "10") int size){
        List<BoardListResponse> boardList = boardService.getBoardList(page, size);
        if(boardList.isEmpty()){
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND).body(ApiResponse.success("게시글이 존재하지 않습니다.", null));
        }
        return ResponseEntity
                .ok(ApiResponse.success(boardList));
    }

    // 게시글 조회 - id
    @GetMapping("/{boardid}")
    public ResponseEntity<ApiResponse<BoardResponse>> viewBoard(@PathVariable("boardid") Integer boardId,
                                                                @RequestParam(required = false) Long cursorId,
                                                                @RequestParam(defaultValue = "15") int pageSize){
        if(!boardRepository.existsById(boardId)){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.fail("존재하지 않는 게시글입니다. id: " + boardId));
        }

        BoardResponse boardResponse = boardService.getBoardById(boardId, cursorId, pageSize);
        return ResponseEntity
                .ok(ApiResponse.success("게시글 조회 성공", boardResponse));
    }

    // TODO: 유저가 작성한 게시글 조회

    // 게시글 수정
    @PatchMapping("/{boardid}/edit")
    public ResponseEntity<ApiResponse<BoardResponse>> editBoard(@PathVariable("boardid") Integer boardId, BoardUpdate boardUpdate){
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
    public ResponseEntity<ApiResponse<String>> deleteBoard(@PathVariable("boarid") Integer boardId){
        if(!boardRepository.existsById(boardId)){
            return ResponseEntity.badRequest()
                    .body(ApiResponse.fail("존재하지 않는 게시글입니다. id: " + boardId));
        }

        boardService.delete(boardId);
        return ResponseEntity.ok(ApiResponse.success("게시글 삭제 성공 id: " + boardId));
    }


}
