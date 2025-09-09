package com.tev.tev.board.controller;

import com.tev.tev.board.dto.BoardCreate;
import com.tev.tev.board.dto.BoardEdit;
import com.tev.tev.board.entity.Board;
import com.tev.tev.board.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/board")
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;
    // 파라미터로 변경

    // 게시글 생성
    @PostMapping("/create")
    public String createdBoard(BoardCreate boardCreate){
        boardService.create(boardCreate);
        return "게시글 생성 완료";
    }

    // 게시글 모두 조회
    @GetMapping("/list")
    public List<Board> listBoard(){
        return boardService.list();
    }

    // 게시글 조회 - id
    @GetMapping("/view/{boardid}")
    public Board viewBoard(@PathVariable("boardid") Integer boardId){
        return boardService.view(boardId);
    }

    // 유저가 작성한 게시글 조회
    @GetMapping("/list/{userid}")
    public List<Board> userListBoard(@PathVariable("userid") Integer userId){
        return boardService.userList(userId);
    }

    // 게시글 수정
    @PatchMapping("/edit")
    public String editBoard(@RequestParam("boardid") Integer boardId, BoardEdit boardEdit){
        boardService.edit(boardId, boardEdit);
        return "게시글 수정 완료 id: " + boardId;
    }

    @DeleteMapping("/delete")
    public String deleteBoard(@RequestParam("boarid") Integer boardId){
        boardService.delete(boardId);
        return "게시글 삭제 완료 id: " + boardId;
    }


}
