package practiceprj.ptcprjcjk.error.using.login;

import practiceprj.ptcprjcjk.enums.ErrorCode;

public class LoginErrorException extends RuntimeException{

    private ErrorCode code;
    private String message;

    public LoginErrorException(String msg, ErrorCode code){
        this.code = code;
        this.message = msg;
    }

    public ErrorCode getErrorCode(){
        return code;
    }
}
