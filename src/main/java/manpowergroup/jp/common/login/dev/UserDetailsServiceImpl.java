package manpowergroup.jp.common.login.dev;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import manpowergroup.jp.common.entity.MUser;
import manpowergroup.jp.common.login.AppUser;
import manpowergroup.jp.common.repository.MUserRepository;

@Profile({ "mock", "dev" })
@Slf4j
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	private MUserRepository mUserRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Override //UserDetailsServiceではString nameなのでここでオーバーライド！emailで受け取る
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		MUser user;
		try {
			user = mUserRepository.findByEmail(email)
					.orElseThrow(() -> new UsernameNotFoundException("User not found for email: " + email));
			log.info("✅ MUser loaded: email={}, oid={}", user.getEmail(), user.getOid()); 
		} catch (Exception e) {
			// DBアクセスや予期せぬ例外が発生した場合
			throw new UsernameNotFoundException("Failed to retrieve user due to system error", e);
		}

		return new AppUser(user);
	}

}