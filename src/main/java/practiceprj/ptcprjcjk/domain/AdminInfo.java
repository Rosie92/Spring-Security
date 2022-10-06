package practiceprj.ptcprjcjk.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

@Entity
@Getter
@Setter
@DynamicUpdate
@Table(name = "admin_info")
public class AdminInfo extends BaseEntityRegDate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value = "admin_no", notes="고유번호")
    @Column(name = "admin_no")
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
