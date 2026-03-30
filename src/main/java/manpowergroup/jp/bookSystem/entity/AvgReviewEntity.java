package manpowergroup.jp.bookSystem.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import com.querydsl.core.annotations.Immutable;

@Entity
@Immutable // 読み取り専用ビュー
@Table(name = "avg_table")
public class AvgReviewEntity {

	@Id
	@Column(name = "isbn_code")
	private String isbnCode;

	@Column(name = "avg_review")
	private Double avgReview;

	// getter/setter（または Lombok の @Getter など）
}
