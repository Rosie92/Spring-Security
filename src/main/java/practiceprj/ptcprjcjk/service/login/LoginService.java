package practiceprj.ptcprjcjk.service.login;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import practiceprj.ptcprjcjk.security.login.JwtUserDetailsService;
import practiceprj.ptcprjcjk.security.login.PtcUsers;

@Slf4j
@Service // 해당 클래스를 루트 컨테이너에 bean 객체로 생성해줌 == @Component(가시성이 떨어지기에 잘 사용하지 않음) | 서비스 레이어, 내부에서 자바 로직을 처리
public class LoginService {

    @Autowired
    private JwtUserDetailsService userDetailService;

    public void authenticateUser(String id, String pw) throws Exception { // 유저 정보 확인
        final PtcUsers userDetails = (PtcUsers) userDetailService.loadUserByUsername(id); // 유저 ID 확인
        BCryptPasswordEncoder passEncoder = new BCryptPasswordEncoder();
        if(!passEncoder.matches(pw, userDetails.getPassword())) { // PW 확인
            throw new BadCredentialsException("아이디 또는 비밀번호가 다릅니다.");
        }
    }

    public void authenticateAdmin(String id, String pw) { // 관리자 정보 확인
        final PtcUsers userDetails = (PtcUsers) userDetailService.loadAdminByUsername(id); // 관리자 ID 확인
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        if(!passwordEncoder.matches(pw, userDetails.getPassword())) { // PW 확인
            throw new BadCredentialsException("아이디 또는 비밀번호가 다릅니다.");
        }
    }
}
