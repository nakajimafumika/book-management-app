package manpowergroup.jp.bookSystem.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import manpowergroup.jp.bookSystem.dto.LoanStatusDto;
import manpowergroup.jp.bookSystem.dto.ReserveStatusDto;
import manpowergroup.jp.bookSystem.entity.BookLoansEntity;
import manpowergroup.jp.bookSystem.entity.BookReservesEntity;
import manpowergroup.jp.bookSystem.repository.jpa.BookLoanJpaRepository;
import manpowergroup.jp.bookSystem.repository.jpa.BookReserveJpaRepository;

@Service
public class BookStatusService {

	private final BookLoanJpaRepository bookLoanJpaRepository;
	private final BookReserveJpaRepository bookReserveJpaRepository;

	// コンストラクタ
	public BookStatusService(BookLoanJpaRepository bookLoanJpaRepository,
			BookReserveJpaRepository bookReserveJpaRepository) {
		this.bookLoanJpaRepository = bookLoanJpaRepository;
		this.bookReserveJpaRepository = bookReserveJpaRepository;
	}

	//ログインユーザーが貸出している本一覧（貸出・予約状況一覧画面）
	public List<LoanStatusDto> getLoanStatus(String oid) {
		return bookLoanJpaRepository.findByUserOidAndReturnDateIsNull(oid).stream()
				.map((BookLoansEntity entity) -> new LoanStatusDto(
						entity.getId(),
						entity.getBook().getTitle(),
						entity.getReturnDueDate()))
				.collect(Collectors.toList());
	}

	// ログインユーザーが予約している本一覧（貸出・予約状況一覧画面）
	public List<ReserveStatusDto> getReserveStatus(String oid) {
		return bookReserveJpaRepository.findByUserOidAndLoanCompleteFlag(oid, 0).stream()
				.map((BookReservesEntity entity) -> new ReserveStatusDto(
						entity.getId(),
						entity.getBook().getTitle(),
						entity.getReservesDate()))
				.collect(Collectors.toList());

	}
}
