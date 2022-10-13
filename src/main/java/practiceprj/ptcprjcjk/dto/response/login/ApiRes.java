package practiceprj.ptcprjcjk.dto.response.login;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class ApiRes<T> {
    private int statusCode;
    private int count = 1;
    private T data;

    public ApiRes(int statusCode, int count, T data) {
        this.statusCode = statusCode;
        this.count = count;
        this.data = data;
    }

    public ApiRes(int statusCode, T data) {
        this.statusCode = statusCode;
        this.data = data;
    }
}
