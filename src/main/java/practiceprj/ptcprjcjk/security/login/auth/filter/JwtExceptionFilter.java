package practiceprj.ptcprjcjk.security.login.auth.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.JwtException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;

@Component // 해당 클래스를 루트 컨테이너에 bean 객체로 생성해줌
public class JwtExceptionFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        try {
            filterChain.doFilter(request, response); // Error가 없어도 필터를 거치기에, 없을 경우
        } catch (JwtException e) {
            setErrorResponse(request, response, e); // Error가 있을 경우의 처리
        }
    }

    private void setErrorResponse(HttpServletRequest request, HttpServletResponse response, JwtException e) throws IOException {
        response.setContentType("application/json; charset=UTF-8");
        HashMap<String, String> body = new HashMap<>();
        body.put("status", String.valueOf(HttpServletResponse.SC_UNAUTHORIZED));
        body.put("error", "Unauthorized");
        body.put("message", e.getMessage());
        body.put("path", request.getServletPath());

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.writeValue(response.getOutputStream(), body);
    }
}
