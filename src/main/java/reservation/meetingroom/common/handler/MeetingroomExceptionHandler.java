package reservation.meetingroom.common.handler;

import reservation.meetingroom.common.api.ApiResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
@Slf4j
@RestControllerAdvice
public class MeetingroomExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(reservation.meetingroom.common.exception.MeetingroomException.class)
    public ResponseEntity<ApiResult> handleOrderException(reservation.meetingroom.common.exception.MeetingroomException e) {
        return ResponseEntity.status(HttpStatus.OK).body(ApiResult.of(e));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResult> handleException(Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiResult.of(e));
    }

}