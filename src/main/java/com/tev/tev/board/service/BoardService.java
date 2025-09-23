package com.tev.tev.board.service;

import com.tev.tev.board.dto.request.BoardRequest;
import com.tev.tev.board.dto.response.BoardListResponse;
import com.tev.tev.board.dto.response.BoardResponse;
import com.tev.tev.board.dto.request.BoardUpdate;
import com.tev.tev.board.entity.Board;
import com.tev.tev.board.repository.BoardRepository;
import com.tev.tev.comment.dto.CommentResponse;
import com.tev.tev.comment.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final CommentService commentService;

    // 게시글 생성
    public Integer saveBoard(BoardRequest boardRequest){
        // TODO: 유저 정보 확인

        // 요청받은 게시글 내용을 저장
        Board board = Board.builder()
                .title(boardRequest.getTitle())
                .content(boardRequest.getContent())
                .createdAt(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .modifiedAt(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .build();

        // save() : DB 저장 후 지정된 엔티티 객체를 반환함.
        boardRepository.save(board);
        return board.getBoardId();
    }

    // 게시글 전체 조회
    public List<BoardListResponse> getBoardList(int page, int size){
        Pageable pageable = PageRequest.of(page, size);

        Page<Board> boards = boardRepository.findAllByOrderByBoardIdDescCreatedAtDesc(pageable);
        return boards.stream().map(BoardListResponse::from).toList();
    }

    // 게시글 상세 조회
    public BoardResponse getBoardById(Integer boardId, Long cursorId, int pageSize){
        Board board = boardRepository.findById(boardId).orElseThrow(() ->
                new IllegalArgumentException("존재하지 않는 게시판입니다. id: " + boardId));

        // 조회수 증가
        board.upViewCount();
        board.setViewCount(board.getViewCount());
        boardRepository.save(board);

        List<CommentResponse> commentResponses = commentService.commentGetList(cursorId, pageSize);
        return BoardResponse.from(board, commentResponses);
    }

    // TODO: 게시글 조회 - 유저 id

    // 게시글 수정
    public BoardResponse updateBoard(Integer boardId, BoardUpdate boardUpdate){
        Board board = boardRepository.findById(boardId).orElseThrow(() ->
                new IllegalArgumentException("존재하지 않는 게시판입니다. id: " + boardId));

        if(boardUpdate.update(board)){
            board.setModifiedAt(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
            boardRepository.save(board);
            return BoardResponse.from(board, null);
        }

        //TODO: 커스텀 예외처리로 변경 ()
        return null;
    }

    // 게시글 삭제
    public void delete(Integer boardId){
        Board board = boardRepository.findById(boardId).orElseThrow(() ->
                new IllegalArgumentException("존재하지 않는 게시글입니다. id: " + boardId));

        boardRepository.delete(board);
    }
}
