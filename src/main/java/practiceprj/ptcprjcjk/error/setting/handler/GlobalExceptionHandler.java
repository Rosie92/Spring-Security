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
@RestController // Spring MVC Controller에 @ResponseBody가 추가된 형태. 주 용도는 Json 형태로 객체 데이터를 반환할 때 사용됨. 리턴할 때 모든 데이터를 Json 형태로 return
@Slf4j // 로그
public class GlobalExceptionHandler {

    @Autowired
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
    /*@RequestBody & @ResponseBody ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┓
    요청본문 & 응답본문
    스프링 비동기 처리를 위해 사용
    본문에 담기는 데이터 형식은 여러 가지가 있지만 가장 대표적으로 사용되는 것이 Json
    비동기식 클라이언트-서버 통신을 위해 Json 형식의 데이터를 주고받기 위해 사용
    스프링 MVC에서도 클라이언트에서 전송한 xml 데이터나 json 등 데이터를 컨트롤러에서 DOM객체나 자바 객체로 변환해서 송수신 할 수 있음
    @RequestBody annotation과 @ResponseBody annotation이 각각 HTTP요청 바디를 자바 객체로 변환하고 자바 객체를 다시 HTTP 응답 바디로 변환해 줌
    @RequestBody를 통해서 자바 객체로 conversion을 하는데, 이 때 HttpMessageConverter를 사용
    @ResponseBody가 붙은 파라미터에는 HTTP 요청의 본문 body 부분이 그대로 전달됨
    @RequestBody annotation이 붙은 파라미터에는 http요청의 본문(body)이 그대로 전달됨,
    일반적인 get/post의 요청 파라미터라면 해당 annotation을 사용할 일이 없음.
    반면에 xml이나 json 기반의 메시지를 사용하는 요청의 경우에는 해당 방법이 매우 유용. http 요청의 바디 내용을 통째로 자바 객체로 변환해서 매피된 메소드 파라미터로 전달해줌
    @ResponseBody는 자바 객체를 HTTP요청의 바디 내용으로 매핑하여 클라이언트로 전송. 해당 annotation을 사용하면 http요청 body를 자바 객체로 전달 받을 수 있음*/
    @ExceptionHandler(CjkRuntimeException.class) // 만들어진 오류 클래스로부터 CjkRuntimeException을 거쳐 이곳으로 오게됨 ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┛
    public ResponseEntity<ErrorInfo> handleException (final CjkRuntimeException e) {
        log.error("--- handleException handler --- {}", e.getLocalizedMessage());
        List<String> errorMessage = new ArrayList<>();
        errorMessage.add(e.getLocalizedMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).
                body(new ErrorInfo(998, "", errorMessage));
    }
}