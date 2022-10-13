package practiceprj.ptcprjcjk.controller.login;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import practiceprj.ptcprjcjk.dto.request.SignInReq;
import practiceprj.ptcprjcjk.dto.request.SignUpReq;
import practiceprj.ptcprjcjk.dto.response.login.ApiRes;
import practiceprj.ptcprjcjk.dto.response.login.SignInRes;
import practiceprj.ptcprjcjk.dto.response.login.SignUpRes;
import practiceprj.ptcprjcjk.service.login.LoginService;

import javax.validation.Valid;

@Slf4j // 로그
@RequiredArgsConstructor // 생성자 주입시 @Autowired 대신 사용. 순환 참조를 방지할 수 있음
@RestController // Spring MVC Controller에 @ResponseBody가 추가된 형태. 주 용도는 Json 형태로 객체 데이터를 반환할 때 사용됨. 리턴할 때 모든 데이터를 Json 형태로 return
public class LoginController {

    private final LoginService _loginService;
    /* ┏━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┓
       ┃ RESTful 의 Backend이기에 Front에서 요청을 전달받고 데이터를 받아 실행　┃
       ┗━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┛ */

    @ApiOperation(value = "sign-up", notes = "회원가입 처리 URL") // swagger
    @PostMapping("/sign-up")
    public ApiRes<SignUpRes> signUpUser(@RequestBody @Valid //유효성 검사. 컨트롤러에서만 동작
                        /* @RequestBody 요청본문 & @ResponseBody 응답본문
                        스프링 비동기 처리를 위해 사용
                        본문에 담기는 데이터 형식은 여러 가지가 있지만 가장 대표적으로 사용되는 것이 Json
                        비동기식 클라이언트-서버 통신을 위해 Json 형식의 데이터를 주고받기 위해 사용
                        스프링 MVC에서도 클라이언트에서 전송한 xml 데이터나 json 등 데이터를 컨트롤러에서 DOM객체나 자바 객체로 변환해서 송수신 할 수 있음
                        @RequestBody annotation과 @ResponseBody annotation이 각각 HTTP요청 바디를 자바 객체로 변환하고 자바 객체를 다시 HTTP 응답 바디로 변환해 줌
                        @RequestBody를 통해서 자바 객체로 conversion을 하는데, 이 때 HttpMessageConverter를 사용
                        @ResponseBody가 붙은 파라미터에는 HTTP 요청의 본문 body 부분이 그대로 전달됨
                        @RequestBody annotation이 붙은 파라미터에는 http요청의 본문(body)이 그대로 전달됨, 일반적인 get/post의 요청 파라미터라면 해당 annotation을 사용할 일이 없음. 반면에 xml이나 json 기반의 메시지를 사용하는 요청의 경우에는 해당 방법이 매우 유용. http 요청의 바디 내용을 통째로 자바 객체로 변환해서 매피된 메소드 파라미터로 전달해줌
                        @ResponseBody는 자바 객체를 HTTP요청의 바디 내용으로 매핑하여 클라이언트로 전송. 해당 annotation을 사용하면 http요청 body를 자바 객체로 전달 받을 수 있음*/
                                                    SignUpReq request) {
        log.debug("--- 회원가입 시도 --- {}", request.getUsername() + "," + request.getPassword());
        return _loginService.signUp(request);
    }

    @ApiOperation(value = "sign-in", notes = "로그인 처리 URL")
    @PostMapping("/sign-in")
    public ApiRes<SignInRes> signInUser(@RequestBody @Valid SignInReq request) {
        log.debug("--- 로그인 시도 --- {}", request.getUsername() + "," + request.getPassword());
        return _loginService.signIn(request);
    }

    /* SpringSecurity의 Logout 처리는 Jwt + Redis를 활용하여 구현 가능
        토큰 발행 시 토큰 두 개를 발급받고(액세스, 리프레시) 로그인 시 두 개를 모두 확인하며 로그아웃 시 기능에 해당하는 토큰을 제거하는 것으로 로그아웃 처리를 구현
        https://wildeveloperetrain.tistory.com/61 참고*/

    /*
    @GetMapping("/sign-in/{userId}")
    public UserInfo findUser(@PathVariable *//* URL에 변수를 넣어 전달할 때 사용 *//* int userId){
        return use_loginService.findUser(userId);
    }

    @GetMapping("/api/test")
    public String test() {
        return "hello!";
    }
    */
}