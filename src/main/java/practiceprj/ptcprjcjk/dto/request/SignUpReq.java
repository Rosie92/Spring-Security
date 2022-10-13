package practiceprj.ptcprjcjk.dto.request;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@NoArgsConstructor
@Data
public class SignUpReq {
    @Size(min = 3, max = 20, message = "Please enter between 3 and 20 characters")
    @NotBlank(message = "Please enter your username")
    private String username;

    @Size(min = 8, max = 30, message = "Please enter between 8 and 30 characters")
    @NotBlank(message = "Please enter your password")
    private String password;
}
