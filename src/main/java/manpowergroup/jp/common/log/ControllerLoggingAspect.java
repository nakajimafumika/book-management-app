package manpowergroup.jp.common.log;

import java.util.Arrays;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class ControllerLoggingAspect {
	private static final Logger log = LoggerFactory.getLogger(ControllerLoggingAspect.class);

	// コントローラーパッケージ配下の全メソッドを対象
	@Before("execution(* manpowergroup.jp.bookSystem.controller..*(..))")
	public void logControllerCall(JoinPoint jp) {
		String method = jp.getSignature().toShortString();
		Object[] args = jp.getArgs();
		log.info("呼ばれたコントローラー.メソッド: {} 引数 {}", method, Arrays.toString(args));
	}
}
