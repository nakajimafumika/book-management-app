package manpowergroup.jp.bookSystem.batch;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import manpowergroup.jp.bookSystem.repository.jpa.BookReserveJpaRepository;

@Component
public class LoanCompleteFlagBatch {

	private final BookReserveJpaRepository bookReserveJpaRepository;

	public LoanCompleteFlagBatch(BookReserveJpaRepository bookReserveJpaRepository) {
		this.bookReserveJpaRepository = bookReserveJpaRepository;
	}

	@Scheduled(cron = "0 */30 * * * *") // 30分ごとに実行
	@Transactional
	public void updateLoanCompleteFlags() {
		int updated = bookReserveJpaRepository.expireUnloanedReservations();
		System.out.println("✅ 期限切れ予約の失効件数: " + updated);
	}

	//	@Scheduled(cron = "0 * * * * *") // 毎分実行（テスト用）
	//	@Transactional
	//	public void updateLoanCompleteFlags() {
	//		System.out.println("🔁 バッチ処理開始");
	//		int updated = bookReserveJpaRepository.expireUnloanedReservations();
	//		System.out.println("✅ 失効件数: " + updated);
	//	}

}
