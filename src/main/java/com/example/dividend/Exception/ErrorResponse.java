package com.example.dividend.Exception;

import lombok.Builder;
import lombok.Data;

/**
 * 에러가 발생했을때 response 해줄 모델 클래스
 */

@Data
@Builder
public class ErrorResponse {
    private int code;
    private String message;
}
