package manpowergroup.jp.common.login;

import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;

import manpowergroup.jp.common.entity.MUser;

public class LoginUser extends User {
	private final MUser mLoginUser;
	private String name;
	
	//このクラスは使ってない！AppUserでログインする(このクラス消していい)
	
	public LoginUser(MUser user) {
        // スーパークラスのユーザーID、パスワードに値をセットする
        // 実際の認証はスーパークラスのユーザーID、パスワードで行われる

		super(
		            user.getEmail(),	
		            user.getPassword(),
		            AuthorityUtils.createAuthorityList(
		                user.getAuthority() == 1 ? "ROLE_ADMIN" : "ROLE_USER"
		            )
		        );

        this.mLoginUser = user;
        this.name = user.getName(); 
    }

	public String getName() {
	    return name;
	}

	public MUser getUser() {
        return mLoginUser;
    }
}
