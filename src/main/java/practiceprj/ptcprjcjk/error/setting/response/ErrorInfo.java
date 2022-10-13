package practiceprj.ptcprjcjk.error.setting.response;

import lombok.*;

import java.util.List;

@Getter
@Setter
// ━━━━━━━━━━━━━━━ 세트 어노테이션 ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┓
@Builder // 자동으로 해당 클래스에 빌더를 추가
@NoArgsConstructor // 파라미터가 없는 기본 생성자 생성
@AllArgsConstructor // 모든 필드 값을 파라미터로 받는 생성자를 생성
// ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┛
public class ErrorInfo {
    private Integer errorCode;
    private String errorMessage;
    private List<String> errorMessages;
}
