package practiceprj.ptcprjcjk.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import practiceprj.ptcprjcjk.domain.UserInfo;

public interface UserRepository extends JpaRepository<UserInfo, String> {

}
