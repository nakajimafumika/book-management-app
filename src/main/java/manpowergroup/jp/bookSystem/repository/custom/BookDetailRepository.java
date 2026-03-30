package manpowergroup.jp.bookSystem.repository.custom;

import java.util.List;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Repository;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;

import manpowergroup.jp.bookSystem.dto.BookDetailDto;
import manpowergroup.jp.bookSystem.entity.QAvgReviewEntity;
import manpowergroup.jp.bookSystem.entity.QBookListsEntity;
import manpowergroup.jp.bookSystem.entity.QBookLoansEntity;
import manpowergroup.jp.bookSystem.entity.QBookReservesEntity;
import manpowergroup.jp.bookSystem.entity.QBookReviewsEntity;
import manpowergroup.jp.bookSystem.entity.QReserveInfo;
import manpowergroup.jp.common.entity.QMUser;

/**
 * 書籍の詳細情報を取得するためのリポジトリクラスです。
 * 
 * QueryDSL を使用して、書籍の基本情報や関連データをデータベースから取得します。
 * 
 * @see BookDetailDto 書籍詳細情報のDTO
 * @see JPAQueryFactory QueryDSLのクエリファクトリ
 */
@Repository
public class BookDetailRepository {

	private final JPAQueryFactory queryFactory;

	// コンストラクタ
	public BookDetailRepository(JPAQueryFactory queryFactory) {
		this.queryFactory = queryFactory;
	}

	// 本の詳細を取得してDTOにつめる
	public BookDetailDto getBookDetailById(Integer id) {

		// ログインユーザーのOIDを取得
		String currentOid = SecurityContextHolder.getContext().getAuthentication().getName();

		// QueryDSL用のQクラスのインスタンス
		QBookListsEntity b = QBookListsEntity.bookListsEntity;
		QBookLoansEntity l = QBookLoansEntity.bookLoansEntity;
		QBookReservesEntity r = QBookReservesEntity.bookReservesEntity;
		QBookReviewsEntity reviews = QBookReviewsEntity.bookReviewsEntity;
		QMUser loanUser = QMUser.mUser;

		// QueryDSL用のQクラスのインスタンス（ビュー）
		QAvgReviewEntity avg = QAvgReviewEntity.avgReviewEntity;// ビューから評価を取得
		QReserveInfo reserveInfo = QReserveInfo.reserveInfo; // ビューから取得する予約情報

		BookDetailDto dto = queryFactory
				.select(Projections.constructor(BookDetailDto.class, // BookDetailDto のコンストラクタに渡す引数を定義
						// 本の基本情報
						b.id,
						b.managementId,
						b.isbnCode,
						b.title,
						b.author,
						b.publisher,
						b.publicationDate,
						b.bookImageName,

						// 貸出情報
						l.loanDate,
						l.returnDueDate,
						loanUser.name,
						// loans
						new CaseBuilder()
								.when(l.book.managementId.isNotNull())
								.then(true)
								.otherwise(false),

						// 予約情報
						reserveInfo.reservesDate,
						reserveInfo.reserveUser,

						// ログインユーザーがこの本を予約済みかどうかを判定
						// BookDetailDto の alreadyReserved フィールドに true/false をセットする
						JPAExpressions
								.selectOne()
								.from(r)
								.where(
										r.user.oid.isNotNull()
												.and(r.user.oid.eq(currentOid))
												.and(r.book.managementId.eq(b.managementId))
												.and(r.loanCompleteFlag.eq(0)))
								.exists(),

						// レビュー情報
						avg.avgReview))
				.from(b)
				.leftJoin(l).on(l.book.managementId.eq(b.managementId).and(l.returnDate.isNull()))
				.leftJoin(l.user, loanUser)// @ManyToOneでBookLoan エンティティの user フィールドが MUserのuser エンティティへの関連になっている
				.leftJoin(reserveInfo).on(reserveInfo.managementId.eq(b.managementId))
				.leftJoin(avg).on(avg.isbnCode.eq(b.isbnCode))

				.where(b.id.eq(id))
				.fetchOne();// 実際にクエリを実行して、1件の結果を取得

		// コメント一覧を取得してセット
		List<String> comments = queryFactory
				.select(reviews.comment)
				.from(reviews)
				.where(reviews.isbnCode.eq(dto.getIsbnCode()))
				.fetch();
		dto.setComments(comments);

		return dto;
	}
}
