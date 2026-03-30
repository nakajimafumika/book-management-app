package manpowergroup.jp.common.login.prod;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import manpowergroup.jp.common.entity.MUser;

@Service
@Profile({ "staged", "prod" })
public class LoginUserService {

	@Autowired
	private LoginUserRepository loginUserRepository;

	public MUser findOrCreateUser(Map<String, Object> attributes) {
		String email = (String) attributes.get("email");
		String name = (String) attributes.get("name");
		String oid = (String) attributes.get("oid");

		return loginUserRepository.findByOid(oid)
				.orElseGet(() -> {
					MUser newUser = new MUser();
					newUser.setEmail(email);
					newUser.setName(name);
					newUser.setOid(oid);
					return loginUserRepository.save(newUser);
				});
	}
}
