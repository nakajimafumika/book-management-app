package manpowergroup.jp.bookSystem.service;

import java.time.LocalDate;

import jakarta.transaction.Transactional;

import org.springframework.stereotype.Service;

import manpowergroup.jp.bookSystem.entity.BookLoansEntity;
import manpowergroup.jp.bookSystem.entity.BookReservesEntity;
import manpowergroup.jp.bookSystem.repository.jpa.BookLoanJpaRepository;
import manpowergroup.jp.bookSystem.repository.jpa.BookReserveJpaRepository;

@Service
@Transactional
public class BookReturnAndCancelService {

	private final BookLoanJpaRepository loanRepo;
	private final BookReserveJpaRepository reserveRepo;

	public BookReturnAndCancelService(BookLoanJpaRepository loanRepo,
			BookReserveJpaRepository reserveRepo) {
		this.loanRepo = loanRepo;
		this.reserveRepo = reserveRepo;
	}

	// 返却処理
	public void returnBook(Long loanId) {
		BookLoansEntity loan = loanRepo.findById(loanId)
				.orElseThrow(() -> new IllegalArgumentException("貸出情報が見つかりません"));

		loan.setReturnDate(LocalDate.now());
		loanRepo.save(loan);
	}

	// 予約キャンセル処理
	public void cancelReserve(Long reserveId) {
		BookReservesEntity reserve = reserveRepo.findById(reserveId)
				.orElseThrow(() -> new IllegalArgumentException("予約情報が見つかりません"));
		reserve.setLoanCompleteFlag(2);
		reserveRepo.save(reserve);
	}
}
