package practiceprj.ptcprjcjk.dto.response.login;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

public class LoginRes {
    @Builder
    @Data /* @Getter / @Setter, @ToString, @EqualsAndHashCode와 @RequiredArgsConstructor를 합쳐놓은 종합 선물 세트 ━━━┓
    POJO (Plain Old Java Objects) 와 bean과 관련된 모든 보일러 플레이트 (boilerplate = 재사용 가능한 코드) 를 생성함
    (class의 모든 필드에 대한 getter, setter, toString, equals와 같은 함수들 말이다) ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┛*/
    @ToString // Lombok Annotation, toString() 메소드를 생성함
    public static class DataResponse {
        private int status;
        private String message;
        private Object data;
    }

}
