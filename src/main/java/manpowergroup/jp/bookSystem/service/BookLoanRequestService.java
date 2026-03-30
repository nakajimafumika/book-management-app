package manpowergroup.jp.bookSystem.service;

import java.time.LocalDate;

import jakarta.transaction.Transactional;

import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.stereotype.Service;

import manpowergroup.jp.bookSystem.entity.BookListsEntity;
import manpowergroup.jp.bookSystem.entity.BookLoansEntity;
import manpowergroup.jp.bookSystem.entity.BookReservesEntity;
import manpowergroup.jp.bookSystem.form.BookLoanForm;
import manpowergroup.jp.bookSystem.repository.custom.BookListRepository;
import manpowergroup.jp.bookSystem.repository.custom.BookLoanRepository;
import manpowergroup.jp.bookSystem.repository.jpa.BookLoanJpaRepository;
import manpowergroup.jp.bookSystem.repository.jpa.BookReserveJpaRepository;
import manpowergroup.jp.common.entity.MUser;
import manpowergroup.jp.common.exception.ExceptionTranslator;
import manpowergroup.jp.common.repository.MUserRepository;

@Service
public class BookLoanRequestService {

	private final BookLoanJpaRepository bookLoanJpaRepository;
	private final BookListRepository bookListRepository;
	private final MUserRepository mUserRepository;
	private final BookReserveJpaRepository bookReserveJpaRepository;

	//コンストラクタ
	public BookLoanRequestService(BookLoanJpaRepository bookLoanJpaRepository,
			BookListRepository bookListRepository,
			MUserRepository mUserRepository,
			BookReserveJpaRepository bookReserveJpaRepository,
			BookLoanRepository bookLoanRepository) {
		this.bookLoanJpaRepository = bookLoanJpaRepository;
		this.bookListRepository = bookListRepository;
		this.mUserRepository = mUserRepository;
		this.bookReserveJpaRepository = bookReserveJpaRepository;
	}

	@Transactional //処理中に例外が発生した場合、ロールバック。正常に登録できたらコミット。
	public void registerLoan(BookLoanForm request, String oid) {

		// 書籍とユーザーを取得
		BookListsEntity book = bookListRepository.findByManagementId(request.getManagementId())
				.orElseThrow(() -> new IllegalArgumentException("管理番号が存在しません: " + request.getManagementId()));

		MUser user = mUserRepository.findByOid(oid)
				.orElseThrow(() -> new IllegalArgumentException("ユーザーOIDが存在しません: " + oid));

		// 貸出エンティティを作成
		BookLoansEntity entity = new BookLoansEntity();
		entity.setBook(book);
		entity.setUser(user);
		entity.setLoanDate(request.getLoanDate());
		entity.setReturnDueDate(request.getReturnDueDate());

		//　ログインユーザーの入力から受け取る貸出期間
		LocalDate loanStart = request.getLoanDate();
		LocalDate loanEnd = request.getReturnDueDate();

		// ログインユーザーの予約開始日と終了日を取得（フラグ=0）
		BookReservesEntity reserve = bookReserveJpaRepository.findLoginUserReserves(request.getManagementId(), oid);
		if (reserve != null) {
			LocalDate reserveStart = reserve.getReservesDate();
			LocalDate reserveEnd = reserve.getReturnDueDate();

			// 重複判定
			boolean isOverlapping = !reserveStart.isAfter(loanEnd) &&
					!loanStart.isAfter(reserveEnd);

			//フラグを０から１に変更
			if (isOverlapping) {
				reserve.setLoanCompleteFlag(1);
				bookReserveJpaRepository.save(reserve); // 予約テーブルフラグ更新
			}

		}

		// 貸出登録（トリガーエラー対応）
		try {
			bookLoanJpaRepository.save(entity);
		} catch (JpaSystemException ex) {
			throw ExceptionTranslator.translate(ex);
		}

	}
}