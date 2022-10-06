package practiceprj.ptcprjcjk.error.using.login;

import practiceprj.ptcprjcjk.error.setting.CjkRuntimeException;

public class AdminException {
    public static class AdminNotFoundException extends CjkRuntimeException{
        public AdminNotFoundException() { super("관리자를 찾을 수 없습니다."); }
    }

    public static class AdminDuplicateException extends CjkRuntimeException{
        public AdminDuplicateException() { super("중복된 관리자ID 입니다."); }
    }
}
