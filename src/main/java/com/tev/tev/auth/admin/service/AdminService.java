package com.tev.tev.auth.admin.service;

import com.tev.tev.auth.user.dto.UserBlockCreate;
import com.tev.tev.auth.user.dto.UserBlockResponse;
import com.tev.tev.auth.user.dto.UserDetailResponse;
import com.tev.tev.auth.user.dto.UserListResponse;
import com.tev.tev.auth.user.entity.Block;
import com.tev.tev.auth.user.entity.Roles;
import com.tev.tev.auth.user.entity.User;
import com.tev.tev.auth.user.repository.BlockRepository;
import com.tev.tev.auth.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final UserRepository userRepository;
    private final BlockRepository blockRepository;

    // 유저 정보 전체 조회 - 오프셋
    public List<UserListResponse> getUserList(String name, int page, int size){
        Pageable pageable = PageRequest.of(page, size);

        List<User> userList;
        if(!StringUtils.hasText(name)) {
            userList = userRepository.findAllByOrderByUserIdDescCreatedAtDesc(pageable);
            return userList.stream()
                    .map(UserListResponse::from)
                    .toList();
        }

        userList = userRepository.findByNicknameContainingOrEmailContainingOrderByUserIdDescCreatedAtDesc(name, name, pageable);
        return userList.stream()
                .map(UserListResponse::from)
                .toList();
    }

    // 유저 상세 정보 조회
    public UserDetailResponse getUserDetails(int userId){
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저입니다. id=" + userId));

        return UserDetailResponse.from(user);
    }

    // 차단 추가
    public void userBlockCreate(UserBlockCreate userBlockCreate){

        Integer userId = userBlockCreate.getUserId();

        User user = userRepository.findById(userBlockCreate.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저입니다."));

        if(blockRepository.existsByBlockedUserId(userId)){
            if(user.getRole() == Roles.ROLE_BLOCK){
                throw new IllegalArgumentException("이미 차단된 유저입니다. userId=" + userId);
            }
        }

        // 차단 목록 추가
        Block block = userBlockCreate.toEntity();
        blockRepository.save(block);

        // 해당 유저의 권한을 BLOCK로 변경
        user.setRole(Roles.ROLE_BLOCK);
        userRepository.save(user);
    }

    // 차단 취소
    public void userBlockDelete(Integer blockedUserId){
        System.out.print("userId=" + blockedUserId);

        // 차단 목록 삭제
        Block block = blockRepository.findByBlockedUserId(blockedUserId);
        blockRepository.delete(block);

        // 해당 유저 권한 USER 변경
        User user = userRepository.findById(blockedUserId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저입니다. blockedUserId=" + blockedUserId));
        user.setRole(Roles.ROLE_USER);
        userRepository.save(user);
    }

    public boolean userBlockCheck(String email){
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저입니다. email=" + email));

        Roles userRole = user.getRole();
        Integer userId = user.getUserId();

        if(userRole == Roles.ROLE_BLOCK ||  blockRepository.existsByBlockedUserId(userId)){
            Block block = blockRepository.findByBlockedUserId(userId);

            if(block == null){
                userBlockDelete(userId);
                return false;
            }

            LocalDateTime expiryAt = block.getExpiryAt();
            boolean isExpired = expiryAt.isBefore(LocalDateTime.now());

            // 만료일이 안지났을 시
            if(isExpired){
                userBlockDelete(userId);
                return false;
            } else {
                return true;
            }
        }
        return false;
    }

    public UserBlockResponse userBlock(String email){
        if(userBlockCheck(email)){
            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저입니다."));

            Integer userId = user.getUserId();

            Block block = blockRepository.findByBlockedUserId(userId);

            return new UserBlockResponse()
                    .from(email + "은 현재 차단된 상태입니다.", block);
        }

        return null;
    }
}
