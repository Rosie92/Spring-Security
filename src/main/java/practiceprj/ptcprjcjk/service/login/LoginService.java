package practiceprj.ptcprjcjk.service.login;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import practiceprj.ptcprjcjk.security.login.JwtUserDetailsService;
import practiceprj.ptcprjcjk.security.login.PtcUsers;

@Slf4j
@Service
public class LoginService {

    @Autowired // 의존성 주입
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
