package practiceprj.ptcprjcjk.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@MappedSuperclass
@Getter
@Setter
public class BaseEntityRegDate {
    @CreationTimestamp /* Insert Query가 발생할 때 현재 시간을 값으로 채워 쿼리를 생성 ━━━━━━━━━━━━━━━━┓
    @InsertTimeStamp 를 사용하면 데이터가 생성된 시점에 대한 관리의 수고스러움을 덜 수 있음
    비슷한 내용으로 @UpdateTimestamp가 있음 - Update Query가 발생할 때마다 마지막 수정 시간을 업데이트  ━━┛*/
    @Column(name = "reg_date")
    @ApiModelProperty(value = "reg_date", notes="가입/등록 일자")
    private LocalDateTime reg_date;
}
