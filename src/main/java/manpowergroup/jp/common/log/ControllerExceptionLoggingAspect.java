package manpowergroup.jp.common.log;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class ControllerExceptionLoggingAspect {

	private static final Logger log = LoggerFactory.getLogger(ControllerExceptionLoggingAspect.class);

	// コントローラーパッケージ配下の全メソッドで例外が投げられたとき
	@AfterThrowing(pointcut = "execution(* manpowergroup.jp.bookSystem..*(..))", throwing = "ex")
	public void logControllerException(JoinPoint jp, Throwable ex) {
		String method = jp.getSignature().toShortString();
		log.error("例外発生  method: {} - Exception: {}", method, ex.getMessage(), ex);
	}

	// 共通例外ハンドラ（manpowergroup.jp.common.exception パッケージ配下）のメソッドが呼ばれたとき
	@Before("execution(* manpowergroup.jp.common.exception..*.*(..))")
	public void logExceptionHandlerInvocation(JoinPoint jp) {
		String method = jp.getSignature().toShortString();
		log.warn("例外ハンドラ呼び出し: {}", method);
	}
}
