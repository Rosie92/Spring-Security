package practiceprj.ptcprjcjk.security.login;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.SpringSecurityCoreVersion;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.util.Assert;

import javax.security.auth.message.AuthException;
import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

@Getter
// ━━━━━━━━━━━━━━━ 세트 어노테이션 ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┓
@Builder // 자동으로 해당 클래스에 빌더를 추가
@NoArgsConstructor // 파라미터가 없는 기본 생성자 생성
@AllArgsConstructor // 모든 필드 값을 파라미터로 받는 생성자를 생성
// ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┛
public class PtcUsers implements UserDetails {
    private static final long serialVersionUID = SpringSecurityCoreVersion.SERIAL_VERSION_UID;

    private static final Log logger = LogFactory.getLog(PtcUsers.class);

    private int user_no;
    private String user_id;
    private String user_pw;
    private Set<GrantedAuthority> authority;
    //private HashMap<String, Object> other_info;

    @Builder.Default // 빌더 패턴을 통해 인스턴스를 만들 때 특정 필드를 특정 값으로 초기화
    private List<String> roles = new ArrayList<>();

    public PtcUsers(int user_no, String id, String password, Collection<? extends GrantedAuthority> authority/*, HashMap<String, Object> otherInfo*/) {
        this.user_no = user_no;
        this.user_pw = password;
        this.user_id = id;
        //this.other_info = otherInfo;

        this.authority = Collections.unmodifiableSet(sortAuthorities(authority));
    }
    public static PtcUsers getAuthorizedUsers(HttpServletRequest request) throws AuthException {
        return getAuthorizedUsers();
    }
    public static PtcUsers getAuthorizedUsers() throws AuthException {
        Object user = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(user instanceof PtcUsers) {
            checkAuthorizedByRole((PtcUsers) user, "USER");
            return (PtcUsers) user;
        }

        throw new UsernameNotFoundException("Not AuthorizedUser");
    }


    public static PtcUsers getAuthorizedAdmin(HttpServletRequest request) throws AuthException {
        return getAuthorizedAdmin();
    }
    public static PtcUsers getAuthorizedAdmin() throws AuthException {
        Object user = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(user instanceof PtcUsers){
            checkAuthorizedByRole((PtcUsers) user, "ADMIN");
            return (PtcUsers) user;
        }
        throw new UsernameNotFoundException("Not AuthorizedUser");
    }
    public static PtcUsers getAuthorizedCompanyOrAuthor() throws AuthException {
        Object user = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(user instanceof PtcUsers){
            checkAuthorizedByRole((PtcUsers) user, "PARTNER","AUTHOR","EMPLOYEE");
            return (PtcUsers) user;
        }
        throw new UsernameNotFoundException("Not AuthorizedUser");
    }

    private static void checkAuthorizedByRole(PtcUsers user, String... role){
        for(String str : role){
            if(user.getRoles().get(0).contains(str)) return;
        }
        throw new UsernameNotFoundException("Not AuthorizedUser");
    }

    public void setUser_no(int user_no) {
        this.user_no = user_no;
    }

    public void setId(String id) {
        this.user_id = id;
    }

    public void setAuthority(Set<GrantedAuthority> authority) {
        this.authority = authority;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roles.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
    }

    public void setAuthorities(Collection<? extends GrantedAuthority> authorities) {
        this.authority = Collections.unmodifiableSet(sortAuthorities(authorities));
    }

    @Override
    public String getPassword() {
        return this.user_pw;
    }

    @Override
    public String getUsername() {
        return getUser_id();
    }

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY) // Json을 자바 객체로 역직렬화할 때 또는 그 반대일 때, Json에 담긴 Key이름과 변수명이 다른 문제가 발생할 때 사용 (Key 매핑)
    @Override // 해당 메소드가 부모 클래스에 있는 메서드를 Override 했다는 것을 명시적으로 선언
    public boolean isAccountNonExpired() {
        // TODO Auto-generated method stub
        return true;
    }
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Override
    public boolean isAccountNonLocked() {
        // TODO Auto-generated method stub
        return true;
    }
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Override
    public boolean isCredentialsNonExpired() {
        // TODO Auto-generated method stub
        return true;
    }
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Override
    public boolean isEnabled() {
        return true;
    }

    private static SortedSet<GrantedAuthority> sortAuthorities(Collection<? extends GrantedAuthority> authorities){
        Assert.notNull(authorities, "Cannot pass a null GrantedAuthority collection");

        SortedSet<GrantedAuthority> sortedAuthorities =
                new TreeSet<GrantedAuthority>(new AuthorityComparator());

        for(GrantedAuthority grantedAuthority : authorities) {
            Assert.notNull(grantedAuthority, "GratedAuthority list cannot contain any null elements");
            sortedAuthorities.add(grantedAuthority);
        }

        return sortedAuthorities;
    }

    private static class AuthorityComparator implements Comparator<GrantedAuthority>, Serializable {
        private static final long serialVersionUID = SpringSecurityCoreVersion.SERIAL_VERSION_UID;

        @Override
        public int compare(GrantedAuthority o1, GrantedAuthority o2) {
            if (o2.getAuthority() == null) return -1;
            if(o1.getAuthority() == null) return 1;

            return o1.getAuthority().compareTo(o2.getAuthority());
        }
    }
}

