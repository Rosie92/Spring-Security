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
@Table(name = "user_info")
public class UserInfo extends BaseEntityRegDate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value = "user_no", notes="고유번호")
    @Column(name = "user_no")
    private int user_no;

    @ApiModelProperty(value = "user_id", notes="유저ID", required = true)
    @Column(name = "user_id")
    private String user_id;

    @ApiModelProperty(value = "user_pw", notes="유저PW", required = true)
    @Column(name = "user_pw")
    private String user_pw;

    @ApiModelProperty(value = "user_use_status", notes="탈퇴여부", required = true)
    @Column(name = "use_status")
    private Boolean use_status;
}
