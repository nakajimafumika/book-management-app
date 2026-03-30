package manpowergroup.jp.bookSystem.log;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import manpowergroup.jp.bookSystem.dto.BookLoanDto;
import manpowergroup.jp.bookSystem.dto.BookReserveDto;

@Aspect
@Component
public class ReserveAndLoanLog {

	private static final Logger log = LoggerFactory.getLogger(ReserveAndLoanLog.class);

	// BookReserveDto を返すメソッドに適用
	@AfterReturning(pointcut = "execution(manpowergroup.jp.bookSystem.dto.BookReserveDto manpowergroup.jp.bookSystem.controller.api.BookReserveApiController.getReserveInfo(..))", returning = "dto")
	public void logReserveInfo(JoinPoint joinPoint, BookReserveDto dto) {
		if (dto == null) {
			log.warn("⚠️ BookReserveDto が null です: {}", joinPoint.getSignature());
			return;
		}

		log.info("📅貸出中の期間: {}", dto.getLoanPeriodDates());
		log.info("📅予約済みの期間: {}", dto.getReservedDates());
		log.info("ログインユーザーの予約件数: {}", dto.isOverReserveLimit());

		if (dto.getLoanPeriodDates() == null || dto.getLoanPeriodDates().isEmpty()) {
			log.info("⚠️ 貸出中の期間データが存在しません");
		}

		if (dto.getReservedDates() == null || dto.getReservedDates().isEmpty()) {
			log.info("⚠️ 予約済みの期間データが存在しません");
		}
	}

	// BookLoanDto を返すメソッドに適用
	//Advice(@AfterReturningなど）は「アスペクトがメソッドに挿入する処理」のこと。
	@AfterReturning(pointcut = "execution(manpowergroup.jp.bookSystem.dto.BookLoanDto manpowergroup.jp.bookSystem.controller.api.BookLoanApiController.getLoanInfo(..))", returning = "dto")
	public void logLoanInfo(JoinPoint joinPoint, BookLoanDto dto) {
		if (dto == null) {
			log.warn("⚠️ BookLoanDto が null です: {}", joinPoint.getSignature());
			return;
		}

		log.info("📅 他ユーザーの予約期間: {}", dto.getOtherReservedDates());
		log.info("📅 ログインユーザーの予約期間: {}", dto.getLoginuserReservedDates());
		log.info("ログインユーザーの貸出件数: {}", dto.isOverLoanLimit());

		if (dto.getOtherReservedDates() == null || dto.getOtherReservedDates().isEmpty()) {
			log.info("⚠️ 他ユーザーの予約データが存在しません");
		}

		if (dto.getLoginuserReservedDates() == null || dto.getLoginuserReservedDates().isEmpty()) {
			log.info("⚠️ ログインユーザーの予約データが存在しません");
		}
	}

	@AfterThrowing(pointcut = "execution(* manpowergroup.jp..*.*(..))", throwing = "ex")
	public void logException(JoinPoint joinPoint, Throwable ex) {
		log.error("❌ 例外発生: {} → {}", joinPoint.getSignature(), ex.getMessage(), ex);
	}
}