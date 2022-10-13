package practiceprj.ptcprjcjk.security.login.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import practiceprj.ptcprjcjk.domain.UserInfo;
import practiceprj.ptcprjcjk.error.using.login.UserException;
import practiceprj.ptcprjcjk.repository.UserRepository;

import java.util.ArrayList;
import java.util.Collection;

@RequiredArgsConstructor
@Service // 해당 클래스를 루트 컨테이너에 bean 객체로 생성해줌 == @Component(가시성이 떨어지기에 잘 사용하지 않음) | 서비스 레이어, 내부에서 자바 로직을 처리
public class CustomUserDetailService implements UserDetailsService {

    private final UserRepository _userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {  // SpringSecurity에서의 내부 동작
        UserInfo userInfo = _userRepository.findByUsername(username).orElseThrow(UserException.UserNotFoundException::new);

        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(userInfo.getRole().toString()));
        // UserDetails안의 User
        return new org.springframework.security.core.userdetails.User(userInfo.getUsername(), userInfo.getPassword(), authorities);
    }
}
