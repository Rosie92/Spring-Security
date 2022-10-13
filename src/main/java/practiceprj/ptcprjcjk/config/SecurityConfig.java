package practiceprj.ptcprjcjk.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;
import practiceprj.ptcprjcjk.security.login.auth.JwtAuthenticationEntryPoint;
import practiceprj.ptcprjcjk.security.login.auth.filter.JwtAuthenticationFilter;
import practiceprj.ptcprjcjk.security.login.auth.filter.JwtExceptionFilter;
import practiceprj.ptcprjcjk.security.login.auth.provider.JwtProvider;

import javax.servlet.FilterChain;

@RequiredArgsConstructor // 생성자 주입시 @Autowired 대신 사용. 순환 참조를 방지할 수 있음
@Configuration // 스프링 IOC Container에게 해당 클래스를 Bean 구성 Class 임을 알려줌
@EnableWebSecurity/* SpringSecurity 활성화 [ (debug = true) 옵션:request가 올 때마다 어떤 filter를 사용하고 있는지 출력 ]*/
/* 🧾 Servlet Container의 Filter ...
        ┃ 서블릿 컨테이너의 Filter는 Dispatch Survlet으로 가기 전에 먼저 적용됨
        ┃ Filter들은 여러개가 연결되어 있어 Filter Chain이라고도 불림
        ┃ 모든 Request들은 FilterChain을 거쳐야 Servlet에 도착하게 됨 */
public class SecurityConfig {

    private final JwtProvider jwtProvider;
    private final JwtAuthenticationEntryPoint authenticationEntryPoint;
    private final JwtExceptionFilter jwtExceptionFilter;

    private static final String[] PERMIT_URL = {
            /* swagger v2 */
            "/v2/api-docs",
            "/swagger-resources",
            "/swagger-resources/**",
            "/configuration/ui",
            "/configuration/security",
            "/swagger-ui.html",
            "/webjars/**",
            /* swagger v3 */
            "/v3/api-docs/**",
            "/swagger-ui/**",
            /* sign-in, sign-up */
            "/sign-in",
            "/sign-up",
            "/"
    };

    @Bean //
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    // 로그인 시 적용되는 설정들이며 로그인을 통해 DB데이터 확인 및 토큰 발행 후 해당 필터를 거침
    // SpringSecurity는 Client에서 요청이오면 FilterChainProxy라는 Bean에 의해 SecurityFilter를 거치게 됨
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        /*  public class Stop_SecurityConfig extends WebSecurityConfigurerAdapter {
                와
            @Override
            protected void configure(HttpSecurity http) throws Exception {
                는 옛날 방식.
            public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
                의 사용이 권장됨 (https://spring.io/blog/2022/02/21/spring-security-without-the-websecurityconfigureradapter) */

        http
                .httpBasic().disable() // 스프링 시큐리티 기본 로그인 화면 설정 -> disable : 기본 설정 사용하지 않는 restful 형식
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) // 세션 사용 X -> JWT 사용.
                .and()
                .authorizeRequests()
                .antMatchers(PERMIT_URL).permitAll() // 설정된 url 허용
                .anyRequest().hasRole("USER") // 로그인 정보의 ROLE을 비교하여 USER 에게만 허용
                .and()
                .addFilterBefore(jwtExceptionFilter, JwtAuthenticationFilter.class)
                .addFilterBefore(new JwtAuthenticationFilter(jwtProvider), UsernamePasswordAuthenticationFilter.class) // 인증 객체 생성 완료 후 진행
                /*  🧾Filter 동작 순서(addFilterBefore) ->
                    1. UsernamePasswordAuthenticationFilter
                    2. Authentication Manager (JwtAuthenticationFilter)
                    3. Authentication Provider (jwtProvider)
                    4. UserDetailService (customUserDetailService implements UserDetailsService)
                    5. UserDetail
                        (https://lalwr.blogspot.com/2018/06/spring-security.html 의 필터 동작 순서 이미지 참조)

                    🧾 1에서 2로, 2에서 3로, 순차적 이동
                    - authorizeRequests 정상 동작 시 1에서 2로 이동하여 2수행 후 1수행
                    - authorizeRequests 오류 발생 시
                        1) jwtExceptionFilter
                        2) JwtAuthenticationFilter
                        3) UsernamePasswordAuthenticationFilter */
                .exceptionHandling().authenticationEntryPoint(authenticationEntryPoint) // 모든 에러 핸들링
                .and()
                .csrf()
                    /*.ignoringAntMatchers("/signup")
                    .ignoringAntMatchers("/signin")*/
                    .disable(); // REST API 사용으로 적용 X;

        return http.build();
    }

}
