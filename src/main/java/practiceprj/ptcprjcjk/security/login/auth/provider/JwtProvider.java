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
    /*  ๐งพ Jwt๋ ํค๋, ํ์ด๋ก๋, ์๊ทธ๋์ฒ๋ก ๊ตฌ์ฑ๋์ด์์ https://velog.io/@haebin/JWT-%ED%86%A0%ED%81%B0-%EC%9D%B8%EC%A6%9D
            โ ํค๋(Header) : ํ ํฐ ์ข๋ฅ์ ํด์ ์๊ณ ๋ฆฌ์ฆ ์ ๋ณด๊ฐ ๋ค์ด์์
            โ ํ์ด๋ก๋(Payload) : ํ ํฐ์ ๋ด์ฉ๋ฌผ์ด ์ธ์ฝ๋ฉ๋ ๋ถ๋ถ
            โ ์๊ทธ๋์ฒ(Signature) : ์ผ๋ จ์ ๋ฌธ์์ด๋ก, ์๊ทธ๋์ฒ๋ฅผ ํตํด ํ ํฐ์ด ๋ณ์กฐ๋์๋์ง ์ฌ๋ถ๋ฅผ ํ์ธ
            โ     (์๊ทธ๋์ฒ๋ Jwt SecretKey๋ก ๋ง๋ค์ด์ง. SecretKey๊ฐ ๋ธ์ถ๋๋ฉด ์๋์ง๋ง ์๊ทธ๋์ฒ ์์ฒด๋ ์จ๊ธฐ์ง ์์๋ ๋จ)
    */

   private static final long JWT_EXPIRATION = 1000L * 60 * 60; // 60๋ถ, Token ๋ง๋ฃ์๊ฐ

    @Value("${spring.jwt.secretKey}") // Spring์ผ๋ก properties๋ฅผ ์ฝ์ ๋ ์ฌ์ฉํ๋ ๋ฐฉ๋ฒ ์ค ํ๋
    private String secretKey;

    @PostConstruct /*์์กด์ฑ ์ฃผ์์ด ์ด๋ฃจ์ด์ง ํ ์ด๊ธฐํ๋ฅผ ์ํํ๋ ๋ฉ์๋ class๊ฐ service๋ฅผ ์ํํ๊ธฐ ์ ์ ๋ฐ์. ์ด ๋ฉ์๋๋ ๋ค๋ฅธ ๋ฆฌ์์ค์์ ํธ์ถ๋์ง ์๋๋ผ๋ ์ํ๋จ*/
    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    } // secretKey ์ํธํ

    private final CustomUserDetailService customUserDetailService;

    /*
        ํ ํฐ ์์ฑ.
     */
    public String generateToken(String username) { // ๋ก๊ทธ์ธ ์ Token ๋ฐ๊ธ
        Date now = new Date();
        Date expiration = new Date(now.getTime() + JWT_EXPIRATION); // ํ์ฌ ์๊ฐ์ผ๋ก๋ถํฐ ๋ง๋ฃ์๊ฐ 60๋ถ
        Claims claims = Jwts.claims().setSubject(username); /* Claims : JWT์ Body. JWT ์์ฑ์๊ฐ JWT๋ฅผ ๋ฐ๋์ด๋ค์๊ฒ ์ ์ํ๊ธฐ ๋ฐ๋ผ๋ ์ ๋ณด๋ฅผ ํฌํจ
            ๋ชจ๋  Claim์ ํ๋ฒ์ ์ง์ ํ๋ ๊ฒฝ์ฐ ์์ ๊ฐ์ด Jwts.claims() ๋ฉ์๋๋ฅผ ์ฌ์ฉํ์ฌ claims ์ธ์คํดํธ๋ฅผ ๋ฐ์ ์์ https://samtao.tistory.com/65 */

        return Jwts.builder()
                //.setHeader() Headers ์ค์ 
                .setClaims(claims) // claim ์ค์ 
                .setIssuedAt(now) // ์์ฑ์ผ
                .setExpiration(expiration) // ๋ง๋ฃ์๊ฐ
                .signWith(SignatureAlgorithm.HS256, secretKey) // HS256(ํด์ ์๊ณ ๋ฆฌ์ฆ)๊ณผ Key๋ก Sign
                .compact(); // ์์ฑ
    }

    public Authentication getAuthentication(String token) { // SecurityContextHolder์ Context์ ๋ด๊ธฐ ์ํด UserDetails๊ฐ์ฒด๋ฅผ ํตํด ๋ด์์จ ์ ๋ณด๋ค์ Authentication์ ๋ด์
        // Jwt ํ ํฐ ์์ฑ์ ์๊ทธ๋์ฒ๋ฅผ username์ผ๋ก ์์ฑํ์๊ธฐ์ ํ ํฐ์์ username๊ณผ DB์ username์ด ์ผ์นํ๋์ง ํ์ธ ํ UserDetails ๊ฐ์ฒด์ ๋ด์
        // UserDetails์์ Role์ด ๋ญ์ง, SpringSecurity์์์ ID,PW๊ฐ ๋ง๋์ง, ํ์ฌ ๋ง๋ฃ ์ํ๋ ์ด๋ค์ง ํ์ธ
        UserDetails userDetail = customUserDetailService.loadUserByUsername(getUsername(token));
        return new UsernamePasswordAuthenticationToken(userDetail, "", userDetail.getAuthorities()); // ์ ์ ์ ์ธ์ฆ, ROLE, ๋ง๋ฃ ๋ฑ์ ๋ํ ์ต์ข ํ์ธ
    }

    public String getUsername(String token) { // DB์ username๊ณผ ๋น๊ตํ๊ธฐ ์ํด ํ ํฐ์ setSubject()ํ username ์ถ์ถํ๋ ์์
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

    public boolean validateToken(String token) { // ํ ํฐ ์ ํจ์ฑ ๊ฒ์ฆ
        try {
            /* String ํํ์ ํ ํฐ์ ์ฌ์ฉํ๊ธฐ ์ํ ํํ๋ก ํ์ฑ(Jwts.parse())ํ๊ณ  ํ ํฐ ์์ฑ ์ ์ฌ์ฉํ๋ Key๋ฅผ Set(setSigninKey("KEY".getBytes()))
            ๊ทธ๋ฆฌ๊ณ  ํ ํฐ์ Jws๋ก ํ์ฑ(parseClaimsJws()) ํ ๋ง์ง๋ง์ผ๋ก getBody()๋ฅผ ์ด์ฉํด ์์ ํ ํฐ์ ์ ์ฅํ๋ ๋ฐ์ดํฐ๋ค์ด ๋ด๊ธด claims๋ฅผ ์ป์ด์ด */
            Jws<Claims> claimsJws = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
            return !claimsJws.getBody().getExpiration().before(new Date());
        } catch (SecurityException e) {
            log.error("Invalid JWT signature.");
            throw new JwtException("์๋ชป๋ JWT ์๊ทธ๋์ฒ");
        } catch (MalformedJwtException e) {
            log.error("Invalid JWT token.");
            throw new JwtException("์ ํจํ์ง ์์ JWT ํ ํฐ");
        } catch (ExpiredJwtException e) {
            log.error("Expired JWT token.");
            throw new JwtException("ํ ํฐ ๊ธฐํ ๋ง๋ฃ");
        } catch (UnsupportedJwtException e) {
            log.error("Unsupported JWT token.");
        } catch (IllegalArgumentException e) {
            log.error("JWT token compact of handler are invalid.");
            throw new JwtException("์ ํจํ์ง ์์ JWT ํ ํฐ.");
        }
        return false;
    }

}
