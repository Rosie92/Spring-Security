package practiceprj.ptcprjcjk.dto.response.login;

import lombok.Data;
import lombok.NoArgsConstructor;
import practiceprj.ptcprjcjk.domain.UserInfo;

@NoArgsConstructor
@Data
public class SignUpRes {

    private int userNo;
    private String username;
    private String role;
    public SignUpRes(UserInfo userInfo) {
        this.userNo = userInfo.getUser_no();
        this.username = userInfo.getUsername();
        this.role = userInfo.getRole().toString();
    }
}