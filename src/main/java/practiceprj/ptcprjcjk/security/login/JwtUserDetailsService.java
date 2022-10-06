package practiceprj.ptcprjcjk.security.login;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import practiceprj.ptcprjcjk.domain.AdminInfo;
import practiceprj.ptcprjcjk.domain.UserInfo;
import practiceprj.ptcprjcjk.enums.UserRole;
import practiceprj.ptcprjcjk.error.using.login.AdminException;
import practiceprj.ptcprjcjk.error.using.login.UserException;
import practiceprj.ptcprjcjk.repository.AdminRepository;
import practiceprj.ptcprjcjk.repository.UserRepository;

import java.util.Collections;

/**
 * DB에 회원정보를 확인, 비교하는 Service Class<br />
 *
 */
@Service
public class JwtUserDetailsService implements UserDetailsService {
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private AdminRepository adminRepository;

	public PtcUsers loadUserByUsername(String id) throws UsernameNotFoundException { // 유저 ID 확인
		UserInfo userInfo = userRepository.findById(id).orElseThrow(() -> new UserException.UserNotFoundException());

		if(userInfo != null) {
			// 이미 탈퇴 처리된 회원이라면
			if(userInfo.getUse_status() == true) throw new UsernameNotFoundException("유저를 찾을 수 없습니다.");
			return PtcUsers.builder()
					.user_no(userInfo.getUser_no())
					.user_id(userInfo.getUser_id())
					.user_pw(userInfo.getUser_pw())
					.roles(Collections.singletonList(UserRole.find().toString()))
					.build();
		}else {
			throw new UsernameNotFoundException("유저를 찾을 수 없습니다.");
		}
	}

	public PtcUsers loadAdminByUsername(String id) throws UsernameNotFoundException { // 관리자 ID 확인
		AdminInfo userInfo = adminRepository.findById(id).orElseThrow(() -> new AdminException.AdminNotFoundException());

		if(userInfo != null) {
			// 해당 관리자 계정의 사용 여부가 false 라면
			if(userInfo.getUse_status() == false) throw new UsernameNotFoundException("관리자를 찾을 수 없습니다.");
			return PtcUsers.builder()
					.user_no(userInfo.getAdmin_no())
					.user_id(userInfo.getAdmin_id())
					.user_pw(userInfo.getAdmin_pw())
					.roles(Collections.singletonList(UserRole.findAdmin().toString()))
					.build();
		}else {
			throw new UsernameNotFoundException("관리자를 찾을 수 없습니다.");
		}
	}
}
