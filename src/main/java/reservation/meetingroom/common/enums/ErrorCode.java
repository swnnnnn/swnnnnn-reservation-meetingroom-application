package reservation.meetingroom.common.enums;

import lombok.Getter;

@Getter
public enum ErrorCode {
    JWT_E001(401, "토큰 오류 발생"),
    JWT_E002(404, "토큰 기간 만료"),

    COMM_E000(1000, "Internal Server Error"),
    COMM_E001(1001, "처리 중 에러가 발생하였습니다."),
    COMM_E002(1002, "필수 파라미터가 누락되었습니다."),
    COMM_E003(1003, "유효하지 않은 필드값입니다."),
    COMM_E004(1004, "유효하지 않은 데이터입니다."),
    COMM_E005(1005, "사용할 수 없는 파라미터입니다.");

    private int code;
    private String message;
    
    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }
}