package manpowergroup.jp.bookSystem.repository.jpa;

import java.util.List;

import jakarta.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import manpowergroup.jp.bookSystem.entity.BookReservesEntity;

/**
 * 書籍予約エンティティ（BookReservesEntity）に対する永続化操作を提供するリポジトリ。
 * Spring Data JPA により、標準的な CRUD メソッド（save, findById, delete など）が自動生成されます。
 * カスタムクエリが必要な場合は、このインターフェースにメソッドを追加。
 */
@Repository
public interface BookReserveJpaRepository extends JpaRepository<BookReservesEntity, Long> {

	//ログインユーザーの予約件数が3件を超えているか
	@Query("SELECT CASE WHEN COUNT(r) >= 3 THEN true ELSE false END " +
			"FROM BookReservesEntity r " +
			"WHERE r.user.oid = :oid AND r.loanCompleteFlag = :flag")
	boolean isOverReserveLimit(@Param("oid") String oid, @Param("flag") String loanCompleteFlag);

	// ログインユーザーの予約開始日と終了日を取得
	@Query("SELECT r FROM BookReservesEntity r " +
			"WHERE r.user.oid = :oid " +
			"AND r.book.managementId = :managementId " +
			"AND r.loanCompleteFlag = 0")
	BookReservesEntity findLoginUserReserves(@Param("managementId") String managementId,
			@Param("oid") String oid);

	// バッチ処理予約フラグ（0→2）
	@Modifying
	@Transactional
	@Query("UPDATE BookReservesEntity r SET r.loanCompleteFlag = 2 "
			+ "WHERE r.returnDueDate < CURRENT_DATE "
			+ "AND r.loanCompleteFlag = 0")
	int expireUnloanedReservations();

	// ログインユーザーが予約している本一覧（貸出・予約状況一覧画面）
	List<BookReservesEntity> findByUserOidAndLoanCompleteFlag(String oid, int loanCompleteFlag);
}