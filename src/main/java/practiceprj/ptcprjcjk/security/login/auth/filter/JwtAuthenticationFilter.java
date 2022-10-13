package practiceprj.ptcprjcjk.security.login.auth.filter;

import org.springframework.beans.factory.annotation.Autowired;
import practiceprj.ptcprjcjk.security.login.auth.provider.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RequiredArgsConstructor /* Lombok Annotation
모든 멤버 변수를 초기화시키는 생성자를 생성 final이나 @NonNull인 필드 값만 파라미터로 받는 생성자를 만들어줌*/
public class JwtAuthenticationFilter extends OncePerRequestFilter /* 어느 서블릿 컨테이너에서나 요청당 한 번의 실행을 보장 */ {

    private static final String HEADER = "Authorization";
    private static final String TOKEN_PREFIX = "Bearer ";

    private final JwtProvider jwtProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // JwtProvider로 부터 생성된 토큰을 전달받아 수행하며, JwtProvider에서 만든 토큰을 역으로 해석
        String token = parseJwt(request); // 순수 토큰 값 추출

        if (token != null && jwtProvider.validateToken(token)/* 토큰 유효성 검증*/) {
            // SecurityContextHolder의 Context에 담기 위해 UserDetails객체를 통해 담아온 정보들을 Authentication에 담음
            Authentication authentication = jwtProvider.getAuthentication(token);
            /*  🧾 ScurityContextHolder의 context
                        ┃ 1. 최종적으로 SecurityContextHolder의 context에 인증 정보(and SpringSecurity 동작 모두)가 저장됨
                        ┃         (토큰 생성 시 context에 최종 저장, 토큰 확인 시 context에서 확인)
                        ┃ 2. context에 저장하기 위한 규격은 Authentication
                        ┃ 3. Authentication에는 UserDetails에서 추출한 데이터를 사용  */
            SecurityContextHolder.getContext().setAuthentication(authentication); // Authentication을 context에 저장
        }
        // FilterChainProxy 필터는 doFilter 메서드 실행 시, 다시 SecurityFilterChain이라는 SpringSecurity Filter List를 갖고 있는 Filter에게 인증 처리를 위임함
        filterChain.doFilter(request, response);
    }

    private String parseJwt(HttpServletRequest request) { // 순수 토큰 값 추출
        String headerAuth = request.getHeader(HEADER); // HEADER : "Authorization"

        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith(TOKEN_PREFIX)) {
            return headerAuth.substring(7); // TOKEN_PREFIX("Bearer ")를 제외한 순수 토큰 값만 추출
        }
        return null;
    }
}

