package practiceprj.ptcprjcjk.dto.request;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@NoArgsConstructor
@Data/* @Getter / @Setter, @ToString, @EqualsAndHashCode와 @RequiredArgsConstructor를 합쳐놓은 종합 선물 세트 ━━━┓
    POJO (Plain Old Java Objects) 와 bean과 관련된 모든 보일러 플레이트 (boilerplate = 재사용 가능한 코드) 를 생성함
    (class의 모든 필드에 대한 getter, setter, toString, equals와 같은 함수들 말이다) ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┛*/
public class SignInReq {
    @Size(min = 3, max = 20, message = "Please enter between 3 and 20 characters")
    @NotBlank(message = "Please enter your username")
    private String username;

    @Size(min = 8, max = 30, message = "Please enter between 8 and 30 characters")
    @NotBlank(message = "Please enter your password")
    private String password;
}
