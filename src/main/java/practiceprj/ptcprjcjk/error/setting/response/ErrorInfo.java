package practiceprj.ptcprjcjk.error.setting.response;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ErrorInfo {
    private Integer errorCode;
    private String errorMessage;
    private List<String> errorMessages;
}
