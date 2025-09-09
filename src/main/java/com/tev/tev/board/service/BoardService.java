package com.tev.tev.board.service;

import com.tev.tev.board.dto.BoardCreate;
import com.tev.tev.board.dto.BoardEdit;
import com.tev.tev.board.entity.Board;
import com.tev.tev.board.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;

    // 게시글 생성 비지니스 로직
    public void create(BoardCreate boardCreate){
        Board board = new Board();
        board.setTitle(boardCreate.getTitle());
        board.setContent(boardCreate.getContent());
        board.setUserId(boardCreate.getUserId());
        board.setBoard_createdAt(LocalDateTime.now());
        boardRepository.save(board);
    }

    // 게시글 모두 조회
    public List<Board> list(){
        return boardRepository.findAll();
    }

    // 게시글 조회 - id
    public Board view(Integer boardId){
        return boardRepository.findByBoardId(boardId);
    }

    // 유저가 작성한 게시글 조회
    public List<Board> userList(Integer userId){
        return boardRepository.findByUserId(userId);
    }

    // 게시글 수정
    public void edit(Integer boardId, BoardEdit boardEdit){
        Board board = boardRepository.findByBoardId(boardId);
        board.setTitle(boardEdit.getTitle());
        board.setContent(boardEdit.getContent());
        board.setBoard_editedAt(LocalDateTime.now());
        boardRepository.save(board);
    }

    // 게시글 삭제
    public void delete(Integer boardId){
        boardRepository.deleteById(boardId);
    }
}
