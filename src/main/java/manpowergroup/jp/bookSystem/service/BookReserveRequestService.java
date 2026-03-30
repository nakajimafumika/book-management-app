package manpowergroup.jp.bookSystem.service;

import jakarta.transaction.Transactional;

import org.springframework.stereotype.Service;

import manpowergroup.jp.bookSystem.entity.BookListsEntity;
import manpowergroup.jp.bookSystem.entity.BookReservesEntity;
import manpowergroup.jp.bookSystem.form.BookReserveForm;
import manpowergroup.jp.bookSystem.repository.custom.BookListRepository;
import manpowergroup.jp.bookSystem.repository.jpa.BookReserveJpaRepository;
import manpowergroup.jp.common.entity.MUser;
import manpowergroup.jp.common.exception.ExceptionTranslator;
import manpowergroup.jp.common.repository.MUserRepository;

@Service
public class BookReserveRequestService {
	private final BookReserveJpaRepository bookReserveJpaRepository;
	private final BookListRepository bookListRepository;
	private final MUserRepository mUserRepository;

	//コンストラクタ
	public BookReserveRequestService(BookReserveJpaRepository bookReserveJpaRepository,
			BookListRepository bookListRepository,
			MUserRepository mUserRepository) {
		this.bookReserveJpaRepository = bookReserveJpaRepository;
		this.bookListRepository = bookListRepository;
		this.mUserRepository = mUserRepository;
	}

	@Transactional //処理中に例外が発生した場合、ロールバック。正常に登録できたらコミット。
	public void reserveRequest(BookReserveForm request, String oid) {
		BookReservesEntity entity = new BookReservesEntity();

		//書籍エンティティを取得 
		BookListsEntity book = bookListRepository.findByManagementId(request.getManagementId())
				.orElseThrow(() -> new IllegalArgumentException("該当書籍なし"));

		// ユーザーエンティティを取得 
		MUser user = mUserRepository.findByOid(oid)
				.orElseThrow(() -> new IllegalArgumentException("該当ユーザーなし"));

		//　取得情報を設定
		entity.setBook(book); // management_id を含む BookListsEntity をセット
		entity.setUser(user); // oid を含む MUser をセット
		entity.setReservesDate(request.getReservesDate());// 予約開始日をセット
		entity.setReturnDueDate(request.getReturnDueDate());// 予約終了日をセット

		// 予約登録（トリガーエラー対応）
		try {
			bookReserveJpaRepository.save(entity);
		} catch (Throwable ex) {
			throw ExceptionTranslator.translate(ex);
		}
	}

}
