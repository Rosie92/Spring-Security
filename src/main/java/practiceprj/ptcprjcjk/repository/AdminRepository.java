package practiceprj.ptcprjcjk.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import practiceprj.ptcprjcjk.domain.AdminInfo;

@Repository // 해당 클래스를 루트 컨테이너에 bean 객체로 생성해줌 == @Component(가시성이 떨어지기에 잘 사용하지 않음) | 퍼시스턴스 레이어, DB나 파일 같은 외부 I/O 작업을 처리
public interface AdminRepository extends JpaRepository<AdminInfo, String> {

}
