package practiceprj.ptcprjcjk.security.login;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.DatatypeConverter;
import java.util.Base64;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.function.Function;

@Component // 해당 클래스를 루트 컨테이너에 bean 객체로 생성해줌
public class JwtTokenProvider { // JWT 토큰을 생성 및 검증 모듈

	@Value("${spring.jwt.secret}") // Spring으로 properties를 읽을 때 사용하는 방법 중 하나
	private String secretKey;
	@Value("${jwt.header-name}")
	private String TOKEN_HEADER;
	@Value("${jwt.access.header-name}")
	private String ACCESS_TOKEN_HEADER;

	public static final String AUTHORIZATION = "Authorization";
	public static final String AUTHORIZATION_ACCESS = "AuthorizationAccess";

	private long tokenValidMilisecond = 1000L * 60 * 60 * 24 * 30; // 1달 토큰 유효
	@Autowired
	private JwtUserDetailsService userDetailService;

	@PostConstruct //의존성 주입이 이루어진 후 초기화를 수행하는 메서드. class가 service를 수행하기 전에 발생. 이 메서드는 다른 리소스에서 호출되지 않더라도 수행됨
	protected void init() {
		secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
	}

	// Jwt 토큰 생성
	public String createToken(String userId, List<String> roles) { // 토큰 생성
		Claims claims = Jwts.claims().setSubject(userId);
		claims.put("roles", roles);
		Date now = new Date();
		return Jwts.builder().setClaims(claims) // 데이터
				.setIssuedAt(now) // 토큰 발행일자
				.setExpiration(new Date(now.getTime() + tokenValidMilisecond)) // set Expire Time
				.signWith(SignatureAlgorithm.HS256, secretKey) // 암호화 알고리즘, secret값 세팅
				.compact();
	}
	// Connection Access Token 생성 - 홈페이지 접근 허가 토큰
	public String createAccessToken() {
		Claims claims = Jwts.claims().setSubject("ptcPrjCjk");
		Date now = new Date();
		return Jwts.builder().setClaims(claims) // 데이터
				.signWith(SignatureAlgorithm.HS256, secretKey) // 암호화 알고리즘, secret값 세팅
				.compact();
	}


	// Jwt 토큰으로 인증 정보를 조회
	public Authentication getAuthentication(String token) {
		String roleStr = getUserRole(token).get(0);
		UserDetails userDetails = null;
		if(roleStr.contains("ADMIN")) {
			userDetails = userDetailService.loadAdminByUsername(this.getUserIdFromToken(token));
		} else {
			userDetails = userDetailService.loadUserByUsername(this.getUserIdFromToken(token));
		}

		return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
	}

	public List<String> getUserRole(String token) {
		final Claims claims = getAllClaimsFromToken(token);
		return claims.get("roles",List.class);
	}

	// Request의 Header에서 token 파싱
	public String resolveToken(HttpServletRequest req) {
		Enumeration<String> headers = req.getHeaders(AUTHORIZATION);
		while(headers.hasMoreElements()){
			String value = headers.nextElement();
			if(value.toLowerCase().startsWith(TOKEN_HEADER.toLowerCase())){
				String token = value.substring(TOKEN_HEADER.length()).trim();
				return value.substring(TOKEN_HEADER.length()).trim();
			}
		}
		return Strings.EMPTY;
	}

	// Request의 Header에서 token 파싱
	public String resolveConnectAccessToken(HttpServletRequest req) {
		Enumeration<String> headers = req.getHeaders(AUTHORIZATION_ACCESS);
		while(headers.hasMoreElements()){
			String value = headers.nextElement();
			if(value.toLowerCase().startsWith(ACCESS_TOKEN_HEADER.toLowerCase())){
				String token = value.substring(ACCESS_TOKEN_HEADER.length()).trim();
				return value.substring(ACCESS_TOKEN_HEADER.length()).trim();
			}
		}
		return Strings.EMPTY;
	}

	//retrieve username from jwt token
	public String getUserIdFromToken(String token) {
		return getClaimFromToken(token, Claims::getSubject);
	}
	//retrieve expiration date from jwt token
	public Date getExpirationDateFromToken(String token) {
		return getClaimFromToken(token, Claims::getExpiration);
	}
	public int getUserNoFromToken(String token) {
		final Claims claims = getAllClaimsFromToken(token);
		return claims.get("no",Integer.class);
	}
	public String getEmailFromToken(String token) {
		final Claims claims = getAllClaimsFromToken(token);
		return claims.get("email",String.class);
	}

	public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = getAllClaimsFromToken(token);
		return claimsResolver.apply(claims);
	}

	//for retrieveing any information from token we will need the secret key
	private Claims getAllClaimsFromToken(String token) {
		return Jwts.parser()
				.setSigningKey(DatatypeConverter.parseBase64Binary(secretKey))
				.parseClaimsJws(token)
				.getBody();
	}

	// Jwt 토큰의 유효성 + 만료일자 확인
	public boolean validateToken(String jwtToken) {
		try {
			Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(jwtToken);
			return !claims.getBody().getExpiration().before(new Date());
		} catch (Exception e) {
			return false;
		}
	}

	public boolean validateConnectAccessToken(String jwtToken) {
		String tempToken = createAccessToken();
		if(tempToken.equals(jwtToken)){
			return true;
		}else{
			return false;
		}
	}
}
