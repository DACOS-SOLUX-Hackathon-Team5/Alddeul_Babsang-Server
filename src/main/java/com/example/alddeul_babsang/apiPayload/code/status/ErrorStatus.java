package com.example.alddeul_babsang.apiPayload.code.status;

import com.example.alddeul_babsang.apiPayload.code.BaseErrorCode;
import com.example.alddeul_babsang.apiPayload.code.ErrorReasonDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorStatus implements BaseErrorCode {

    _INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "COMMON500", "서버 에러, 관리자에게 문의 바랍니다."),
    _BAD_REQUEST(HttpStatus.BAD_REQUEST, "COMMON400", "잘못된 요청입니다."),
    _UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "COMMON401", "인증이 필요합니다."),
    _FORBIDDEN(HttpStatus.FORBIDDEN, "COMMON403", "금지된 요청입니다."),


    USER_ERROR_ID(HttpStatus.FORBIDDEN, "USER4001", "존재하지 않은 user id 입니다."),
    STORE_ERROR_ID(HttpStatus.FORBIDDEN, "STORE4001", "존재하지 않은 store id 입니다."),
    FAVORITE_ERROR_ID(HttpStatus.FORBIDDEN, "STORE4001", "존재하지 않은 favorite id 입니다."),

    _FILE_UPLOAD_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "COMMON500", "파일 업로드 실패, 파일을 다시 확인해주세요."),  // 파일 업로드 실패 추가
    _INVALID_FILE_FORMAT(HttpStatus.BAD_REQUEST, "COMMON400", "유효하지 않은 파일 형식입니다."),
    _FILE_TOO_LARGE(HttpStatus.BAD_REQUEST, "COMMON400", "파일 크기가 너무 큽니다. 다시 시도해주세요.");


    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    @Override
    public ErrorReasonDTO getReason() {
        return ErrorReasonDTO.builder()
                .message(message)
                .code(code)
                .build();
    }

    @Override
    public ErrorReasonDTO getReasonHttpStatus() {
        return ErrorReasonDTO.builder()
                .message(message)
                .code(code)
                .httpStatus(httpStatus)
                .build();
    }
}
