package manpowergroup.jp.bookSystem.entity;

import java.time.LocalDate;

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
@Table(name = "t_book_requests")
public class BookRequestsEntity {

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

	@Column(name = "title", length = 100, nullable = false)
	private String title;

	@Column(name = "author", length = 100, nullable = false)
	private String author;

	@Column(name = "book_url", length = 255)
	private String bookUrl;

	@Column(name = "memo", length = 200)
	private String memo;

	@Column(name = "request_date", nullable = false)
	private LocalDate requestDate;

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

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getBookUrl() {
		return bookUrl;
	}

	public void setBookUrl(String bookUrl) {
		this.bookUrl = bookUrl;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public LocalDate getRequestDate() {
		return requestDate;
	}

	public void setRequestDate(LocalDate requestDate) {
		this.requestDate = requestDate;
	}

}