package practiceprj.ptcprjcjk.security.login.auth.provider;

import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import practiceprj.ptcprjcjk.error.setting.CjkRuntimeException;
import practiceprj.ptcprjcjk.security.login.auth.CustomUserDetailService;

import javax.annotation.PostConstruct;
import java.util.Base64;
import java.util.Date;

@Slf4j
@RequiredArgsConstructor
@Component
public class JwtProvider {
    /*  🧾 Jwt는 헤더, 페이로드, 시그니처로 구성되어있음 https://velog.io/@haebin/JWT-%ED%86%A0%ED%81%B0-%EC%9D%B8%EC%A6%9D
            ┃ 헤더(Header) : 토큰 종류와 해시 알고리즘 정보가 들어있음
            ┃ 페이로드(Payload) : 토큰의 내용물이 인코딩된 부분
            ┃ 시그니처(Signature) : 일련의 문자열로, 시그니처를 통해 토큰이 변조되었는지 여부를 확인
            ┃     (시그니처는 Jwt SecretKey로 만들어짐. SecretKey가 노출되면 안되지만 시그니처 자체는 숨기지 않아도 됨)
    */

   private static final long JWT_EXPIRATION = 1000L * 60 * 60; // 60분, Token 만료시간

    @Value("${spring.jwt.secretKey}") // Spring으로 properties를 읽을 때 사용하는 방법 중 하나
    private String secretKey;

    @PostConstruct /*의존성 주입이 이루어진 후 초기화를 수행하는 메서드 class가 service를 수행하기 전에 발생. 이 메서드는 다른 리소스에서 호출되지 않더라도 수행됨*/
    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    } // secretKey 암호화

    private final CustomUserDetailService customUserDetailService;

    /*
        토큰 생성.
     */
    public String generateToken(String username) { // 로그인 시 Token 발급
        Date now = new Date();
        Date expiration = new Date(now.getTime() + JWT_EXPIRATION); // 현재 시간으로부터 만료시간 60분
        Claims claims = Jwts.claims().setSubject(username); /* Claims : JWT의 Body. JWT 생성자가 JWT를 받는이들에게 제시하기 바라는 정보를 포함
            모든 Claim을 한번에 지정하는 경우 위와 같이 Jwts.claims() 메소드를 사용하여 claims 인스턴트를 받아 작업 https://samtao.tistory.com/65 */

        return Jwts.builder()
                //.setHeader() Headers 설정
                .setClaims(claims) // claim 설정
                .setIssuedAt(now) // 생성일
                .setExpiration(expiration) // 만료시간
                .signWith(SignatureAlgorithm.HS256, secretKey) // HS256(해시 알고리즘)과 Key로 Sign
                .compact(); // 생성
    }

    public Authentication getAuthentication(String token) { // SecurityContextHolder의 Context에 담기 위해 UserDetails객체를 통해 담아온 정보들을 Authentication에 담음
        // Jwt 토큰 생성시 시그니처를 username으로 생성하였기에 토큰안의 username과 DB의 username이 일치하는지 확인 후 UserDetails 객체에 담음
        // UserDetails에서 Role이 뭔지, SpringSecurity에서의 ID,PW가 맞는지, 현재 만료 상태는 어떤지 확인
        UserDetails userDetail = customUserDetailService.loadUserByUsername(getUsername(token));
        return new UsernamePasswordAuthenticationToken(userDetail, "", userDetail.getAuthorities()); // 유저의 인증, ROLE, 만료 등에 대한 최종 확인
    }

    public String getUsername(String token) { // DB의 username과 비교하기 위해 토큰에 setSubject()한 username 추출하는 작업
        try {
            return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
        } catch (ExpiredJwtException e) {
            throw new CjkRuntimeException(e) {
                @Override
                public String getLocalizedMessage() {
                    return "Token error";
                }
            };
        }
    }

    public boolean validateToken(String token) { // 토큰 유효성 검증
        try {
            /* String 형태의 토큰을 사용하기 위한 형태로 파싱(Jwts.parse())하고 토큰 생성 시 사용했던 Key를 Set(setSigninKey("KEY".getBytes()))
            그리고 토큰을 Jws로 파싱(parseClaimsJws()) 후 마지막으로 getBody()를 이용해 앞서 토큰에 저장했던 데이터들이 담긴 claims를 얻어옴 */
            Jws<Claims> claimsJws = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
            return !claimsJws.getBody().getExpiration().before(new Date());
        } catch (SecurityException e) {
            log.error("Invalid JWT signature.");
            throw new JwtException("잘못된 JWT 시그니처");
        } catch (MalformedJwtException e) {
            log.error("Invalid JWT token.");
            throw new JwtException("유효하지 않은 JWT 토큰");
        } catch (ExpiredJwtException e) {
            log.error("Expired JWT token.");
            throw new JwtException("토큰 기한 만료");
        } catch (UnsupportedJwtException e) {
            log.error("Unsupported JWT token.");
        } catch (IllegalArgumentException e) {
            log.error("JWT token compact of handler are invalid.");
            throw new JwtException("유효하지 않은 JWT 토큰.");
        }
        return false;
    }

}
