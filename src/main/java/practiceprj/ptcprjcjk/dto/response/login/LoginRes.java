package practiceprj.ptcprjcjk.dto.response.login;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

public class LoginRes {
    @Builder
    @Data
    @ToString
    public static class DataResponse {
        private int status;
        private String message;
        private Object data;
    }

}
