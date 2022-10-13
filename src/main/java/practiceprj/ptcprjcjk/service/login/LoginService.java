package practiceprj.ptcprjcjk.service.login;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import practiceprj.ptcprjcjk.domain.UserInfo;
import practiceprj.ptcprjcjk.dto.request.SignInReq;
import practiceprj.ptcprjcjk.dto.request.SignUpReq;
import practiceprj.ptcprjcjk.dto.response.login.ApiRes;
import practiceprj.ptcprjcjk.dto.response.login.SignInRes;
import practiceprj.ptcprjcjk.dto.response.login.SignUpRes;
import practiceprj.ptcprjcjk.enums.Role;
import practiceprj.ptcprjcjk.error.using.login.UserException;
import practiceprj.ptcprjcjk.repository.UserRepository;
import practiceprj.ptcprjcjk.security.login.auth.provider.JwtProvider;

import static javax.servlet.http.HttpServletResponse.*;

@Slf4j
@Transactional(readOnly = true) /* 클래스나 메서드에 붙여줄 경우, 해당 범위 내 메서드가 트랜잭션이 되도록 보장해줌
        선언적 트랜잭션 : 직접 객체를 만들 필요 없이 선언만으로도 관리를 용이하게 해줌*/
@RequiredArgsConstructor
@Service
public class LoginService {

    private final JwtProvider jwtProvider;
    private final UserRepository _userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public ApiRes<SignUpRes> signUp(SignUpReq request) {
        UserInfo userInfo = UserInfo.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword())) // 암호화
                .role(Role.ROLE_USER)
                .build();

        duplicateValidateUser(userInfo); // 가입 시 중복 ID인지 확인
        UserInfo saveUser = _userRepository.save(userInfo); // 저장
        return new ApiRes<>(SC_CREATED, new SignUpRes(saveUser));
    }

    public ApiRes<SignInRes> signIn(SignInReq request) {
        // ID, PW 확인
        UserInfo userInfo = _userRepository.findByUsername(request.getUsername()).orElseThrow(UserException.UserNotFoundException::new);
        if (!passwordEncoder.matches(request.getPassword(), userInfo.getPassword())) {
            throw new UserException.UserDataNotMatchedException();
        }
        String token = jwtProvider.generateToken(userInfo.getUsername()); // Token 발급
        String message = "success";

        log.info("[JWT]={}", token);

        return new ApiRes<>(SC_OK, new SignInRes(userInfo, message, token));
    }

    private void duplicateValidateUser(UserInfo userInfo) { // 가입 시 중복 ID인지 확인
        if (_userRepository.existsByUsername(userInfo.getUsername())) {
            throw new UserException.UserDuplicateException();
        }
    }

    /*public UserInfo findUser(int userId) {
        return use_userRepository.findById(userId).get();
    }*/
}