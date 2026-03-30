package manpowergroup.jp.bookSystem.repository.custom;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import manpowergroup.jp.bookSystem.entity.BookReviewsEntity;

public interface BookReviewsRepository extends JpaRepository<BookReviewsEntity, Integer> {
	//ISBNコードでレビューを取ってくる(一覧・詳細画面)
	List<BookReviewsEntity> findByIsbnCode(String isbnCode);
}

