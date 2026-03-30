package manpowergroup.jp.bookSystem.service;

import org.springframework.stereotype.Service;

import manpowergroup.jp.bookSystem.dto.BookReserveDto;
import manpowergroup.jp.bookSystem.repository.custom.BookReserveRepository;
import manpowergroup.jp.bookSystem.repository.jpa.BookReserveJpaRepository;

@Service
public class BookReserveService {

	private final BookReserveRepository bookReserveRepository;
	private final BookReserveJpaRepository bookReserveJpaRepository;

	// コンストラクタ
	public BookReserveService(BookReserveRepository bookReserveRepository,
			BookReserveJpaRepository bookReserveJpaRepository) {
		this.bookReserveRepository = bookReserveRepository;
		this.bookReserveJpaRepository = bookReserveJpaRepository;
	}

	public BookReserveDto getReserveAndLoanDates(String managementId, String oid) {

		// ログインユーザーの予約件数の制限
		boolean isOverReserveLimit = bookReserveJpaRepository.isOverReserveLimit(oid, "0");

		// 予約日と貸出日の情報を取得
		BookReserveDto dto = bookReserveRepository.getReserveAndLoanDates(managementId);

		// DTOに予約制限情報を入れる
		dto.setOverReserveLimit(isOverReserveLimit);

		return dto;
	}

}
