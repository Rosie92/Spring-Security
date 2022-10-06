package practiceprj.ptcprjcjk.error.using.login;

import practiceprj.ptcprjcjk.error.setting.CjkRuntimeException;

public class UserException {
    public static class UserNotFoundException extends CjkRuntimeException{
        public UserNotFoundException() { super("유저를 찾을 수 없습니다."); }
    }

    public static class UserDuplicateException extends CjkRuntimeException{
        public UserDuplicateException() { super("중복된 유저 ID 입니다."); }
    }
}
