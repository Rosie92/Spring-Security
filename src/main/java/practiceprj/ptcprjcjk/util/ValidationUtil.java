package practiceprj.ptcprjcjk.util;

import java.util.regex.Pattern;

public class ValidationUtil {
	public static boolean isEmail(String str) {
		return Pattern.matches("^([\\w-]+(?:\\.[\\w-]+)*)@((?:[\\w-]+\\.)*\\w[\\w-]{0,66})\\.([a-z]{2,6}(?:\\.[a-z]{2})?)$", str);
	}
	public static boolean isMemberPw(String str) {
		return Pattern.matches("^(?=.*[A-Za-z])(?=.*\\d)(?=.*[$@$!%*#?&.])[A-Za-z\\d$@$!%*#?&.]{8,32}$", str);
	}
}
