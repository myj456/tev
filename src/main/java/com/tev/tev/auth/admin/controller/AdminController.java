package com.tev.tev.auth.admin.controller;

import com.tev.tev.auth.admin.service.AdminService;
import com.tev.tev.auth.user.dto.UserBlockCreate;
import com.tev.tev.auth.user.dto.UserDetailResponse;
import com.tev.tev.auth.user.dto.UserListResponse;
import com.tev.tev.auth.user.repository.UserRepository;
import com.tev.tev.common.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;
    private final UserRepository userRepository;

    // 유저 정보 전체 조회 및 검색(name, email)
    @GetMapping("/list")
    public ResponseEntity<ApiResponse<List<UserListResponse>>> getUserList(@RequestParam(value = "search", defaultValue = "") String name,
                                                         @RequestParam(value = "page", defaultValue = "0") int page,
                                                         @RequestParam(value = "size", defaultValue = "15") int size){

        List<UserListResponse> userListResponses = adminService.getUserList(name, page, size);
        return ResponseEntity
                .ok(ApiResponse.success(userListResponses));
    }

    // 유저 상제 정보 조회
    @GetMapping("/user/details")
    public ResponseEntity<ApiResponse<UserDetailResponse>> getUserDetails(@RequestParam("userid") Integer userId){
        if(!userRepository.existsById(userId)){
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse
                            .fail("존재하지 않는 유저입니다. userid=" + userId));
        }

        UserDetailResponse userDetailResponse = adminService.getUserDetails(userId);
        return ResponseEntity
                .ok(ApiResponse.success(userDetailResponse));
    }

    // 유저 차단 추가
    @PostMapping("/block")
    public ResponseEntity<ApiResponse<String>> userBlock(@RequestBody UserBlockCreate userBlockCreate){
        adminService.userBlockCreate(userBlockCreate);

        return ResponseEntity
                .ok(ApiResponse.success("차단 완료 userid=" + userBlockCreate.getUserId()));
    }

    // 유저 차단 취소
    @DeleteMapping("/block/cancel")
    public ResponseEntity<ApiResponse<String>> userBlockCancel(@RequestParam("blockuserid") Integer userId){
        adminService.userBlockDelete(userId);

        return ResponseEntity
                .ok(ApiResponse.success("차단 취소 userId=" + userId));
    }
}
