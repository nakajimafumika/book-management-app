package manpowergroup.jp.bookSystem.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import manpowergroup.jp.common.entity.MUser;

@Entity
@Table(name = "t_book_reviews")
public class BookReviewsEntity {

	@ManyToOne
	@JoinColumn(name = "book_id")
	private BookListsEntity book;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;

	// --- 外部キー：oid → m_users.oid ---
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "oid", referencedColumnName = "oid", nullable = false)
	private MUser user;

	@Column(name = "isbn_code", length = 17, nullable = true)
	private String isbnCode;

	@Column(name = "review", nullable = false)
	private Integer review; // TINYINT UNSIGNED → Javaでは Integer

	@Column(name = "comment", length = 200)
	private String comment;
	// --- Getter / Setter ---

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public MUser getUser() {
		return user;
	}

	public void setUser(MUser user) {
		this.user = user;
	}

	public String getIsbnCode() {
		return isbnCode;
	}

	public void setIsbnCode(String isbnCode) {
		this.isbnCode = isbnCode;
	}

	public Integer getReview() {
		return review;
	}

	public void setReview(Integer review) {
		this.review = review;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}
}
