package practiceprj.ptcprjcjk.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;
import practiceprj.ptcprjcjk.enums.Role;

import javax.persistence.*;

@Entity // 테이블과의 매핑, 해당 Annotation이 붙은 class는 JPA가 관리, 필드에 final, enum, interface, class 사용 불가, 생성자 중 기본 생성자가 반드시 필요
@Getter
@DynamicUpdate // JPA Entity에 사용하는 Annotation, 실제 값이 변경된 컬럼으로만 updateQuery를 만드는 기능
@NoArgsConstructor(access = AccessLevel.PROTECTED) // 파라미터가 없는 기본 생성자 생성
@Table(name = "user_info") // @Entity Annotation과 매핑할 테이블(DB)를 지정, 생략 시 매핑한 엔티티 이름을 테이블 이름으로 사용
public class UserInfo extends BaseEntityRegDate {
    @Id // JPA가 객체를 관리할 때 식별할 기본키를 지정
    @GeneratedValue(strategy = GenerationType.IDENTITY)/* ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┓
                                                                                     기본키의 값을 위한 자동 생성 전략을 명시하는데 사용,
선택적 속성 ▶ 1. generator : SequenceGenerator나 TableGenerator Annotation에서 명시된 기본키 생성자를 재사용할 때 사용됨. Default 값-("")
            2. strategy : persistence provider가 Entity의 기본키를 생성할 때 사용해야 하는 기본키 생성 전략을 의미. Default 값-Auto,━━━━━━━┛ */
    @ApiModelProperty(value = "user_no", notes="고유번호")
    @Column(name = "user_no")
    private int user_no;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING) // 자바 enum 타입을 Entity class의 속성으로 사용할 수 있음
    private Role role;

    @Builder // 자동으로 해당 클래스에 빌더를 추가
    public UserInfo(String username, String password, Role role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }
}
