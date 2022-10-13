package practiceprj.ptcprjcjk.dto.response.login;

import lombok.Data;
import lombok.NoArgsConstructor;
import practiceprj.ptcprjcjk.domain.UserInfo;

@NoArgsConstructor
@Data
public class SignInRes {

    private String logInUsername;
    private String token;
    private String message;

    public SignInRes(UserInfo userInfo, String message, String token) {
        this.logInUsername = userInfo.getUsername();
        this.token = token;
        this.message = message;
    }
}
