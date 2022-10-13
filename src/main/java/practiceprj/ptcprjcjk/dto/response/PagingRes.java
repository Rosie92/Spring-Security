package practiceprj.ptcprjcjk.dto.response;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Builder
@Data
@ToString // toString() 메소드를 생성. Lombok Annotation
public class PagingRes {
    private int page;
    private int perpage;
    private int totalpage;
    private int totalrecordcount;
    private Object data;
}
