package com.tev.tev.board.service;

import com.tev.tev.auth.user.entity.User;
import com.tev.tev.auth.user.repository.UserRepository;
import com.tev.tev.board.dto.response.LikeResponse;
import com.tev.tev.board.entity.Board;
import com.tev.tev.board.entity.Likes;
import com.tev.tev.board.repository.BoardRepository;
import com.tev.tev.board.repository.LikeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LikeService {

    private final LikeRepository likeRepository;
    private final UserRepository userRepository;
    private final BoardRepository boardRepository;

    // 좋아요 추가 및 취소
    @Transactional
    public LikeResponse likeBoard(Integer boardId){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserDetails userDetails = (UserDetails) principal;
        String email = userDetails.getUsername();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저입니다. email=" + email));

        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다. boardId=" + boardId));

        boolean exists = likeRepository.existsByUserAndBoard(user, board);

        if(exists){
            likeRepository.deleteByUserAndBoard(user, board);
            board.downLikeCount();
            boardRepository.save(board);

            return LikeResponse.builder()
                    .boardId(board.getBoardId())
                    .likeCount(board.getLikeCount())
                    .userId(user.getUserId())
                    .build();
        }

        Likes likes = Likes.builder()
                .user(user)
                .board(board)
                .build();

        likeRepository.save(likes);

        board.upLikeCount();
        boardRepository.save(board);

        return LikeResponse.from(likes);
    }
}
