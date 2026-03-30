
package manpowergroup.jp.common.config;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.web.SecurityFilterChain;

import manpowergroup.jp.common.entity.MUser;
import manpowergroup.jp.common.login.AppUser;
import manpowergroup.jp.common.login.prod.LoginUserService;

@Configuration
@EnableWebSecurity
@Profile({ "staged", "prod" })
public class EntraSecurityConfig {

	@Autowired
	private LoginUserService loginUserService;

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
				.authorizeHttpRequests(auth -> auth
						.requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
						.anyRequest().authenticated())
				.oauth2Login(oauth2 -> oauth2
						.userInfoEndpoint(userInfo -> userInfo
								.oidcUserService(customOAuth2UserService())));
		return http.build();
	}

	@Bean
	public OAuth2UserService<OidcUserRequest, OidcUser> customOAuth2UserService() {
		return userRequest -> {
			OAuth2UserService<OidcUserRequest, OidcUser> delegate = new OidcUserService();
			OidcUser oidcUser = delegate.loadUser(userRequest);

			Map<String, Object> attributes = oidcUser.getAttributes();
			MUser user = loginUserService.findOrCreateUser(attributes);

			return new AppUser(user, oidcUser, attributes);
		};
	}
}
