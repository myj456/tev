package com.tev.tev.board.service;

import com.tev.tev.auth.user.entity.User;
import com.tev.tev.auth.user.repository.UserRepository;
import com.tev.tev.board.dto.request.BoardRequest;
import com.tev.tev.board.dto.response.BoardListResponse;
import com.tev.tev.board.dto.response.BoardResponse;
import com.tev.tev.board.dto.request.BoardUpdate;
import com.tev.tev.board.entity.Board;
import com.tev.tev.board.repository.BoardRepository;
import com.tev.tev.board.repository.LikeRepository;
import com.tev.tev.comment.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final CommentService commentService;

    private final UserRepository userRepository;
    private final LikeRepository likeRepository;

    // 게시글 생성
    public Integer saveBoard(BoardRequest boardRequest){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserDetails userDetails = (UserDetails) principal;
        String email = userDetails.getUsername();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저입니다. email=" + email));

        // 요청받은 게시글 내용을 저장
        Board board = boardRequest.toEntity(user);

        // save() : DB 저장 후 지정된 엔티티 객체를 반환함.
        boardRepository.save(board);
        return board.getBoardId();
    }

    // 게시글 전체 조회 - 오프셋
    public List<BoardListResponse> searchBoardList(String keyword, int page, int size){
        Pageable pageable = PageRequest.of(page, size);

        List<Board> searchBoards;
        if(!StringUtils.hasText(keyword)){
            searchBoards = boardRepository.findAllByOrderByBoardIdDescCreatedAtDesc(pageable);
            return searchBoards.stream()
                    .map(BoardListResponse::from)
                    .toList();
        }

        searchBoards = boardRepository.findByTitleContainingOrderByBoardIdDescCreatedAtDesc(keyword, pageable);
        return searchBoards.stream()
                .map(BoardListResponse::from)
                .toList();
    }

    // 게시글 상세 조회
    public BoardResponse getBoardById(Integer boardId, Long cursorId, int pageSize){
        Board board = boardRepository.findById(boardId).orElseThrow(() ->
                new IllegalArgumentException("존재하지 않는 게시판입니다. id: " + boardId));

        // 조회수 증가
        board.upViewCount();
        board.setViewCount(board.getViewCount());
        boardRepository.save(board);

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        boolean liked = false; // 기본값은 false(좋아요 안 누름)

        if (principal instanceof UserDetails userDetails) { // 로그인한 사용자인지 확인
            String email = userDetails.getUsername();
            User user = userRepository.findByEmail(email).orElseThrow();
            liked = likeRepository.existsByUserAndBoard(user, board);
        }

        BoardResponse response = BoardResponse.from(board);
        response.setLiked(liked); // 로그인 여부에 따라 결정된 liked 값을 설정

        return response;
    }

    // 게시글 수정
    public BoardResponse updateBoard(Integer boardId, BoardUpdate boardUpdate){
        Board board = boardRepository.findById(boardId).orElseThrow(() ->
                new IllegalArgumentException("존재하지 않는 게시판입니다. id: " + boardId));

        if(boardUpdate.update(board)){
            board.setModifiedAt(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            boardRepository.save(board);
            return BoardResponse.from(board);
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
