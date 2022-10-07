package practiceprj.ptcprjcjk.controller.login;

import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import practiceprj.ptcprjcjk.dto.response.login.LoginRes;
import practiceprj.ptcprjcjk.security.login.JwtTokenProvider;
import practiceprj.ptcprjcjk.security.login.JwtUserDetailsService;
import practiceprj.ptcprjcjk.security.login.PtcUsers;
import practiceprj.ptcprjcjk.service.login.LoginService;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Slf4j // 로그
@Controller // 해당 클래스를 루트 컨테이너에 bean 객체로 생성해줌 == @Component(가시성이 떨어지기에 잘 사용하지 않음) | 프레젠테이션 레이어, 웹 요청과 응답을 처리
public class LoginController {

    @Autowired // 의존성 주입을 위해 사용, 필요한 의존 객체의 '타입'에 해당하는 bean을 자동으로 찾아 주입
    private LoginService loginService;
    @Autowired
    private JwtUserDetailsService jwtUserDetailsService;
    @Autowired
    private JwtTokenProvider jwtTokenProvider;


    // ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━ 로그인 관련 ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

    // ━━━━━ User Login
    @ApiOperation(value = "Login", notes = "사용자 로그인 처리 URL") // swagger
    @PostMapping("/login")
    public ResponseEntity<?> login(String id, String pw) throws Exception {
        log.debug("--- 사용자 로그인 시도 --- {}", id + " : " + pw);
        loginService.authenticateUser(id, pw); // 유저 정보 확인
        String token = jwtTokenProvider.createToken( // 토큰 생성
                id, jwtUserDetailsService.loadUserByUsername(id).getRoles());
        Map<String,Object> tokenMap = new HashMap<>(); // 유저 정보와 token 정보를 담을 hashMap 생성
        final PtcUsers userDetails = (PtcUsers) jwtUserDetailsService.loadUserByUsername(id);
        tokenMap.put("id",id);
        tokenMap.put("token",token);
        tokenMap.put("userNo", userDetails.getUser_no());
        return ResponseEntity.ok(LoginRes.DataResponse.builder()
                .status(200)
                .message("Token access")
                .data(tokenMap)
                .build());
    }

    // ━━━━━ Admin Login
    @ApiOperation(value = "Admin Login", notes = "관리자 로그인 처리 URL") // swagger
    @GetMapping("/admin/login")
    public ResponseEntity<?> adminLogin(@RequestParam("id") String id, @RequestParam("pw") String pw, HttpServletRequest request) throws BadCredentialsException {
        log.debug("--- 관리자 로그인 시도 --- {}", id + " : " + pw);
        loginService.authenticateAdmin(id, pw); // 관리자 정보 확인
        final PtcUsers userDetails = (PtcUsers) jwtUserDetailsService.loadAdminByUsername(id);
        String token = jwtTokenProvider.createToken(
                id, jwtUserDetailsService.loadAdminByUsername(id).getRoles());
        Map<String,Object> tokenMap = new HashMap<>(); // 관리자 정보와 token 정보를 담을 hashMap 생성
        tokenMap.put("id",id);
        tokenMap.put("no", userDetails.getUser_no());
        tokenMap.put("roles", userDetails.getRoles());
        tokenMap.put("token",token);
        return ResponseEntity.ok(LoginRes.DataResponse.builder()
                .status(200)
                .message("Token access")
                .data(tokenMap)
                .build());
    }
}
