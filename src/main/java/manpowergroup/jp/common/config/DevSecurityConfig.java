package manpowergroup.jp.common.config;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

@Profile({ "mock", "dev" })
@Configuration
public class DevSecurityConfig {

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
				.authorizeHttpRequests(auth -> auth
						.requestMatchers("/login", "/css/**", "/images/**", "/js/**", "/error", "/.well-known/**")
						.permitAll()
						.anyRequest().authenticated())
				.formLogin(form -> form
						.loginPage("/login")// ログインページのURL
						.loginProcessingUrl("/login")// 認証処理を行うURL
						.defaultSuccessUrl("/home")//ログインが成功した最初の遷移先

						.usernameParameter("login_email")//入力項目メールアドレスとパスワード
						.passwordParameter("login_password")
						.failureHandler(handleAuthenticationFailure())
						.permitAll())
				.logout(logout -> logout
						.logoutUrl("/logout")
						.logoutSuccessUrl("/login")
						.permitAll()

				)
				.csrf(Customizer.withDefaults()); //Getリクエストはトークン不要(デフォルト設定)

		return http.build();
	}

	@Bean //パスワードハッシュ化
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
		return config.getAuthenticationManager();
	}

	@Bean
	/*ユーザが間違ったメールアドレスやパスワードを入力すると、Spring Security が認証に失敗します。
	 * そのとき AuthenticationFailureHandler が動いて、設定されている URL にリダイレクトします。
	 * リダイレクト時にパラメータが付く URL が /login?error=true になるので、リクエストパラメータ error が存在します。
	*/

	public AuthenticationFailureHandler handleAuthenticationFailure() {
		return new SimpleUrlAuthenticationFailureHandler() {
			@Override
			public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
					AuthenticationException exception) throws IOException, ServletException {
				setDefaultFailureUrl("/login?error=true");
				super.onAuthenticationFailure(request, response, exception);
			}
		};
	}
}
