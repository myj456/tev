package com.tev.tev.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApiResponse<T> {
    private boolean success;
    private String message;
    private T data;

    // 요청 성공 응답 - 데이터
    public static <T> ApiResponse<T> success(T data){
        return new ApiResponse<>(true, "요청 처리 성공", data);
    }

    // 요청 성공 응답 - 메세지 + 데이터
    public static <T> ApiResponse<T> success(String message, T data){
        return new ApiResponse<>(true, message, data);
    }


    // 요청 에러 - 메세지 
    public static <T> ApiResponse<T> error(String message){
        return new ApiResponse<>(false, message, null);
    }

    // 요청 에러 - 메세지 + 데이터 
    public static <T> ApiResponse<T> error(String message, T data){
        return new ApiResponse<>(false, message, data);
    }

    // 요청 실패 - 메세지
    public static <T> ApiResponse<T> fail(String message){
        return new ApiResponse<>(false, message, null);
    }

    public static <T> ApiResponse<T> fail(String message, T data){
        return new ApiResponse<>(false, message, data);
    }
}