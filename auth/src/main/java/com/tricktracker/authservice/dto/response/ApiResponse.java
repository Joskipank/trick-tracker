package com.tricktracker.authservice.dto.response;


import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse<T> {
    private boolean success;
    private String message;
    private T data;
    private ErrorDetailResponse error;

    public static <T> ApiResponse<T> success(String message, T data) {
        var resp = new ApiResponse<T>();
        resp.success = true;
        resp.message = message;
        resp.data = data;
        return resp;
    }

    public static <T> ApiResponse<T> error(int code, String message) {
        return error(code, message, null);
    }


    public static <T> ApiResponse<T> error(int code, String message, String details) {
        var resp = new ApiResponse<T>();
        resp.success = false;
        resp.message = message;
        resp.error = new ErrorDetailResponse(code, details != null ? details : message);
        return resp;
    }


}