package practiceprj.ptcprjcjk.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import practiceprj.ptcprjcjk.domain.UserInfo;

@Repository
public interface UserRepository extends JpaRepository<UserInfo, String> {

}
