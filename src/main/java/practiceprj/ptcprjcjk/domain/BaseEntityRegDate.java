package practiceprj.ptcprjcjk.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@MappedSuperclass // 객체의 입장에서 공통 매핑 정보가 필요할 때 사용
@Getter @Setter // 각각 접근자와 설정자 메소드를 작성해주는 Lombok Annotation
public class BaseEntityRegDate {
    @CreationTimestamp /* Insert Query가 발생할 때 현재 시간을 값으로 채워 쿼리를 생성 ━━━━━━━━━━━━━━━━┓
    @InsertTimeStamp 를 사용하면 데이터가 생성된 시점에 대한 관리의 수고스러움을 덜 수 있음
    비슷한 내용으로 @UpdateTimestamp가 있음 - Update Query가 발생할 때마다 마지막 수정 시간을 업데이트  ━━┛*/
    @Column(name = "reg_date")/* 객체 필드를 테이블 컬럼에 매핑 ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┓
             옵션 ▶ 1. insertable : Entity 저장 시 이 필드도 같이 저장하며, false로 설정하면 DB에 저장하지 않음(읽기 전용일 때 사용)
                   2. updatable : 위와 동일하지만 Update일 때 해당됨 ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┛ */
    @ApiModelProperty(value = "reg_date", notes="가입/등록 일자") /*Swagger에서 해당 Annotation이 붙은 곳에 대한 설명을 확인하기 위함 ━┓
                                   required 옵션은 true일 시 꼭 값을 받아야지만 실행되는 옵션, GeneratedValue의 경우 꼭 붙일 필요 없음 ━┛*/
    private LocalDateTime reg_date;
}
