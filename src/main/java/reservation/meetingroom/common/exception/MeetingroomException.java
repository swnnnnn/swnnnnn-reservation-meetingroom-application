package reservation.meetingroom.common.exception;

import reservation.meetingroom.common.enums.ErrorCode;
import lombok.Getter;

@Getter
public class MeetingroomException extends RuntimeException {

    private int code;
    private String message;

    public MeetingroomException(ErrorCode errorCode){
        this.code = errorCode.getCode();
        this.message = errorCode.getMessage();
    }
}
