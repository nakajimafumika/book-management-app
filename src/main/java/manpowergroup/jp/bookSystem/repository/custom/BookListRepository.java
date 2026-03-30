package manpowergroup.jp.bookSystem.repository.custom;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;

import manpowergroup.jp.bookSystem.dto.BookListDto;
import manpowergroup.jp.bookSystem.entity.BookListsEntity;
import manpowergroup.jp.bookSystem.entity.QAvgReviewEntity;
import manpowergroup.jp.bookSystem.entity.QBookListsEntity;
import manpowergroup.jp.bookSystem.entity.QBookLoansEntity;

@Repository
public class BookListRepository {

	private final JPAQueryFactory queryFactory;

	// QueryDSLを使ったクエリを構築するためのファクトリを注入
	public BookListRepository(JPAQueryFactory queryFactory) {
		this.queryFactory = queryFactory;
	}

	// 本の一覧を取得してDTOにつめる
	public List<BookListDto> getBookList() {
		// エンティティのエイリアス定義
		QBookListsEntity b = QBookListsEntity.bookListsEntity;
		QBookLoansEntity l = QBookLoansEntity.bookLoansEntity;
		QAvgReviewEntity avg = QAvgReviewEntity.avgReviewEntity;

		return queryFactory
				// BookListDto のコンストラクタに渡すフィールドを定義。
				.select(Projections.constructor(BookListDto.class,
						b.id,
						b.managementId,
						b.title,
						b.author,
						// status: 貸出中かどうかを判定
						new CaseBuilder()
								.when(JPAExpressions
										.selectOne()
										.from(l)
										.where(l.book.managementId.eq(b.managementId)
												.and(l.returnDate.isNull()))
										.exists())
								.then("貸出中")
								.otherwise(""),
						avg.avgReview))
				.from(b)
				.leftJoin(avg).on(b.isbnCode.eq(avg.isbnCode))
				.fetch();
	}

	/**管理IDで本を1件取得するためのメソッド。(このメソッドは普通のクエリ名メソッドで実行できるので、JPAのフォルダに移してもいいかも？？？
	 * 呼出し階層はBookLoanRequestServiceの BookLoansEntity registerLoan（予約処理も同様）
	*/
	public Optional<BookListsEntity> findByManagementId(String managementId) {
		QBookListsEntity b = QBookListsEntity.bookListsEntity;

		BookListsEntity result = queryFactory
				.selectFrom(b)
				.where(b.managementId.eq(managementId))
				.fetchOne();

		return Optional.ofNullable(result);
	}

}
