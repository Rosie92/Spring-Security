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
    @CreationTimestamp
    @Column(name = "reg_date", updatable = false)
    @ApiModelProperty(value = "reg_date", notes="가입/등록 일자")
    private LocalDateTime reg_date;
}
