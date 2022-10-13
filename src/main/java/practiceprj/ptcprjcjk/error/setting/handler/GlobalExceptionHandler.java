package practiceprj.ptcprjcjk.error.setting.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import practiceprj.ptcprjcjk.error.setting.CjkRuntimeException;
import practiceprj.ptcprjcjk.error.setting.response.ErrorInfo;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@ControllerAdvice // @ExceptionHandler 가 하나의 클래스에 대한 것이라면, 이것은 모든 @Controller, 즉 전역에서 발생할 수 있는 예외를 잡아 처리해주는 Annotation임
@RestController
@Slf4j // 로그
public class GlobalExceptionHandler {

    @Autowired // 의존성 주입을 위해 사용, 필요한 의존 객체의 '타입'에 해당하는 bean을 자동으로 찾아 주입
    private MessageSource messageSource;

    @ResponseStatus(HttpStatus.BAD_REQUEST) // 해당 코드의 응답의 상태 코드를 지정할 수 있음
    @ExceptionHandler(MethodArgumentNotValidException.class) // Controller, @RestController가 적용된 bean 내에서 발생하는 예외를 잡아서 하나의 메서드에서 처리해주는 기능을 함
    public ErrorInfo handleException(final MethodArgumentNotValidException e, Locale locale) {
        log.error("---- handleException MethodArgumentNotValidException --- {}, {}", locale.getDisplayLanguage(), locale.getDisplayCountry());

        final ErrorInfo errorInfo = new ErrorInfo();
        errorInfo.setErrorCode(HttpStatus.BAD_REQUEST.value());
        errorInfo.setErrorMessages(new ArrayList<>());

        e.getBindingResult().getFieldErrors().stream().forEach(fieldError -> {
            log.info("{}, {}, {}", fieldError.getField(), fieldError.getDefaultMessage());
            errorInfo.getErrorMessages().add(fieldError.getDefaultMessage());
        });

        return errorInfo;
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(HttpClientErrorException.Forbidden.class)
    public HttpClientErrorException.Forbidden forbiddenHandleException(HttpClientErrorException.Forbidden e) {
        log.error("--- FORBIDDEN --- ", e);
        return e;
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(HttpClientErrorException.Unauthorized.class)
    public ResponseEntity<ErrorInfo> unauthorizedHandleException(HttpSession session, HttpClientErrorException.Unauthorized e, Locale locale) {

        log.error("--- UNATHORIZED --- ", e);
        return ResponseEntity.status(e.getStatusCode()).body(new ErrorInfo (e.getStatusCode().value(), messageSource.getMessage("login.error.mismatch", null, locale), null));
    }

    @ResponseStatus(HttpStatus.TOO_MANY_REQUESTS)
    @ExceptionHandler(HttpClientErrorException.TooManyRequests.class)
    public ResponseEntity<ErrorInfo> lockedHandleException(HttpClientErrorException e, Locale locale) {

        log.error("---TOO_MANY_REQUESTS --- ", e);
        return ResponseEntity.status(e.getStatusCode()).body(new ErrorInfo (e.getStatusCode().value(), messageSource.getMessage("login.error.locked", null, locale), null));
    }

    @ExceptionHandler(value=Exception.class)
    public ResponseEntity<ErrorInfo> handleException(Exception e) {
        log.error("--- global exception handler ---", e);
        //e.printStackTrace();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorInfo (999, "Error Occurs", null));
    }


    @ResponseBody
    @ExceptionHandler(CjkRuntimeException.class) // 만들어진 오류 클래스로부터 CjkRuntimeException을 거쳐 이곳으로 오게됨 ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┛
    public ResponseEntity<ErrorInfo> handleException (final CjkRuntimeException e) {
        log.error("--- handleException handler --- {}", e.getLocalizedMessage());
        List<String> errorMessage = new ArrayList<>();
        errorMessage.add(e.getLocalizedMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).
                body(new ErrorInfo(998, "", errorMessage));
    }
}