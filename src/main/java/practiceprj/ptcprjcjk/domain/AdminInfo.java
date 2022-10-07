package practiceprj.ptcprjcjk.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

@Entity // 테이블과의 매핑, 해당 Annotation이 붙은 class는 JPA가 관리, 필드에 final, enum, interface, class 사용 불가, 생성자 중 기본 생성자가 반드시 필요
@Getter @Setter // 각각 접근자와 설정자 메소드를 작성해주는 Lombok Annotation
@DynamicUpdate // JPA Entity에 사용하는 Annotation, 실제 값이 변경된 컬럼으로만 updateQuery를 만드는 기능
@Table(name = "admin_info") // @Entity Annotation과 매핑할 테이블(DB)를 지정, 생략 시 매핑한 엔티티 이름을 테이블 이름으로 사용
public class AdminInfo extends BaseEntityRegDate {
    @Id // JPA가 객체를 관리할 때 식별할 기본키를 지정
    @GeneratedValue(strategy = GenerationType.IDENTITY)/* ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┓
                                                                                     기본키의 값을 위한 자동 생성 전략을 명시하는데 사용,
                                                                                                                     선택적 속성 ▼
            1. generator : SequenceGenerator나 TableGenerator Annotation에서 명시된 기본키 생성자를 재사용할 때 사용됨. Default 값-("")
            2. strategy : persistence provider가 Entity의 기본키를 생성할 때 사용해야 하는 기본키 생성 전략을 의미. Default 값-Auto,━━━━━━━┛
    */
    @ApiModelProperty(value = "admin_no", notes="고유번호") /*Swagger에서 해당 Annotation이 붙은 곳에 대한 설명을 확인하기 위함 ━┓
                              required 옵션은 true일 시 꼭 값을 받아야지만 실행되는 옵션, GeneratedValue의 경우 꼭 붙일 필요 없음 ━┛*/
    @Column(name = "admin_no") /* 객체 필드를 테이블 컬럼에 매핑 ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┓
                                                                                                                  옵션▼
                    1. insertable : Entity 저장 시 이 필드도 같이 저장하며, false로 설정하면 DB에 저장하지 않음(읽기 전용일 때 사용)
                    2. updatable : 위와 동일하지만 Update일 때 해당됨 ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┛ */
    private int admin_no;

    @ApiModelProperty(value = "admin_id", notes="관리자ID", required = true)
    @Column(name = "admin_id")
    private String admin_id;

    @ApiModelProperty(value = "admin_pw", notes="관리자PW", required = true)
    @Column(name = "admin_pw")
    private String admin_pw;

    @ApiModelProperty(value = "admin_code", notes="관리자 코드", required = true)
    @Column(name = "admin_code")
    private String admin_code;

    @ApiModelProperty(value = "admin_use_status", notes="사용여부", required = true)
    @Column(name = "use_status")
    private Boolean use_status;
}
