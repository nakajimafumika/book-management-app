package manpowergroup.jp.common.login;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import manpowergroup.jp.common.entity.MUser;
@Slf4j
@Getter
public class AppUser implements UserDetails, OidcUser {
 //UserDetailsはフォーム認証、OidcUserはOID認証
    private final String oid;
    private final String name;
    private final String email;
    private final List<GrantedAuthority> authorities;
    private final Map<String, Object> attributes;
 
    private final String password;
 
    // OIDC 専用フィールド
    private final Map<String, Object> claims;
    private final OidcIdToken idToken;
    private final OidcUserInfo userInfo;
 
    // コンストラクタ（フォーム認証用）
    public AppUser(MUser user) {
        this.oid = user.getOid();;
        this.name = user.getName();
        this.email = user.getEmail();
        this.attributes = null;
        this.password = user.getPassword();
        this.authorities = null;
        this.claims = Map.of();
        this.idToken = null;
        this.userInfo = null;
    }
 
    // コンストラクタ（OIDC用）
    public AppUser(MUser user, OidcUser oidcUser, Map<String, Object> attributes) {
        this.oid = user.getOid(); //oidを保持する
        this.name = user.getName();
        this.email = user.getEmail();
        this.attributes = attributes;
        this.password = null;
        this.authorities = new ArrayList<>(oidcUser.getAuthorities());
        this.claims = oidcUser.getClaims();
        this.idToken = oidcUser.getIdToken();
        this.userInfo = oidcUser.getUserInfo();
        log.info("AppUser created with OID: {}", this.oid);
    }
 
    // UserDetails 実装
    @Override public String getUsername() { return name; }
    @Override public String getPassword() { return password; }
    @Override public Collection<? extends GrantedAuthority> getAuthorities() { return authorities; }
    @Override public boolean isAccountNonExpired() { return true; }
    @Override public boolean isAccountNonLocked() { return true; }
    @Override public boolean isCredentialsNonExpired() { return true; }
 
    // OidcUser 実装
    @Override public Map<String, Object> getClaims() { return claims; }
    @Override public OidcUserInfo getUserInfo() { return userInfo; }
    @Override public OidcIdToken getIdToken() { return idToken; }
    @Override public String getName() { return name; }
}
 
 