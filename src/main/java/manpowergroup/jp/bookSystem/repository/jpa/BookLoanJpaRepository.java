package manpowergroup.jp.bookSystem.repository.jpa;

import java.util.List;
import java.util.Optional;

import jakarta.persistence.LockModeType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import manpowergroup.jp.bookSystem.entity.BookListsEntity;
import manpowergroup.jp.bookSystem.entity.BookLoansEntity;

/**
 * 書籍貸出エンティティ（BookLoansEntity）に対する永続化操作を提供するリポジトリ。
 * Spring Data JPA により、標準的な CRUD メソッド（save, findById, delete など）が自動生成されます。
 * カスタムクエリが必要な場合は、このインターフェースにメソッドを追加。
 */
@Repository
public interface BookLoanJpaRepository extends JpaRepository<BookLoansEntity, Long> {

	//ログインユーザーの貸出件数が5件を超えているか
	@Query("SELECT CASE WHEN COUNT(l) >= 5 THEN true ELSE false END " +
			"FROM BookLoansEntity l " +
			"WHERE l.user.oid = :oid AND l.returnDate IS NULL")
	boolean isOverLoanLimit(@Param("oid") String oid);

	// 貸出処理の悲観ロック
	@Lock(LockModeType.PESSIMISTIC_WRITE)
	@Query("SELECT b FROM BookListsEntity b WHERE b.managementId = :managementId")
	Optional<BookListsEntity> findByManagementIdForUpdate(@Param("managementId") String managementId);

	// ログインユーザーが貸出している本一覧（貸出・予約状況一覧画面）
	List<BookLoansEntity> findByUserOidAndReturnDateIsNull(String oid);
}
