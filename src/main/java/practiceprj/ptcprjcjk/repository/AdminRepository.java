package practiceprj.ptcprjcjk.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import practiceprj.ptcprjcjk.domain.AdminInfo;

public interface AdminRepository extends JpaRepository<AdminInfo, String> {

}
