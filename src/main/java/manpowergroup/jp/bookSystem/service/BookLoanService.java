package manpowergroup.jp.bookSystem.service;

import org.springframework.stereotype.Service;

import manpowergroup.jp.bookSystem.dto.BookLoanDto;
import manpowergroup.jp.bookSystem.repository.custom.BookLoanRepository;
import manpowergroup.jp.bookSystem.repository.jpa.BookLoanJpaRepository;

@Service
public class BookLoanService {

	private final BookLoanRepository bookLoanRepository;
	private final BookLoanJpaRepository bookLoanJpaRepository;

	//　コンストラクタ
	public BookLoanService(BookLoanRepository bookLoanrepository, BookLoanJpaRepository bookLoanJpaRepository) {
		this.bookLoanRepository = bookLoanrepository;
		this.bookLoanJpaRepository = bookLoanJpaRepository;
	}

	public BookLoanDto getReservedDates(String managementId, String oid) {

		// ログインユーザーの貸出件数の制限
		boolean isOverLoanLimit = bookLoanJpaRepository.isOverLoanLimit(oid);

		// 予約日を取得
		BookLoanDto dto = bookLoanRepository.getReservedDates(managementId, oid);

		// DTOに貸出制限情報を入れる
		dto.setOverLoanLimit(isOverLoanLimit);

		return dto;
	}
}
