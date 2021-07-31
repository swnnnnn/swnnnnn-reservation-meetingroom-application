package reservation.meetingroom.common.api;

import reservation.meetingroom.common.enums.ErrorCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ApiResult<T> {

    private int code;
    private String message;
    private T data;

    public ApiResult(ErrorCode errorCode){
        this.code = errorCode.getCode();
        this.message = errorCode.getMessage();
    }

    public ApiResult(reservation.meetingroom.common.exception.MeetingroomException claimException){
        this.code = claimException.getCode();
        this.message = claimException.getMessage();
    }

    public static ApiResult of(Exception e){
        return new ApiResult(ErrorCode.COMM_E000);
    }

    public static ApiResult of(reservation.meetingroom.common.exception.MeetingroomException e){
        return new ApiResult(e);
    }

    public ApiResult(){
        this.message = "OK";
    }

    public ApiResult(T data){
        this.message = "OK";
        this.data = data;
    }
}
