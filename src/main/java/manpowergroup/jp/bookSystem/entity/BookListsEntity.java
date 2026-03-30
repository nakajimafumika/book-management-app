package manpowergroup.jp.bookSystem.entity;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "m_book_lists")

public class BookListsEntity {
	@OneToMany(mappedBy = "book", fetch = FetchType.LAZY)
	private List<BookLoansEntity> loans;

	@OneToMany(mappedBy = "book", fetch = FetchType.LAZY)
	private List<BookReservesEntity> reserves;

	@OneToMany(mappedBy = "book", fetch = FetchType.LAZY)
	private List<BookReviewsEntity> reviews;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;

	@Column(name = "management_id", length = 20, nullable = false, unique = true)
	private String managementId;

	@Column(name = "isbn_code", length = 17, nullable = true)
	private String isbnCode;

	@Column(name = "title", length = 100, nullable = false)
	private String title;

	@Column(name = "author", length = 100, nullable = false)
	private String author;

	@Column(name = "publisher", length = 30, nullable = false)
	private String publisher;

	@Column(name = "publication_date", nullable = false)
	private String publicationDate;

	@Column(name = "delete_flag", nullable = false)
	private Integer deleteFlag = 0;

	@Column(name = "book_image_name", length = 255, nullable = true)
	private String bookImageName;

	// --- Getter / Setter ---

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getManagementId() {
		return managementId;
	}

	public void setManagementId(String managementId) {
		this.managementId = managementId;
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

	public String getPublisher() {
		return publisher;
	}

	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}

	public String getPublicationDate() {
		return publicationDate;
	}

	public void setPublicationDate(String publicationDate) {
		this.publicationDate = publicationDate;
	}

	public Integer getDeleteFlag() {
		return deleteFlag;
	}

	public void setDeleteFlag(Integer deleteFlag) {
		this.deleteFlag = deleteFlag;
	}

	public List<BookLoansEntity> getLoans() {
		return loans;
	}

	public void setLoans(List<BookLoansEntity> loans) {
		this.loans = loans;
	}

	public List<BookReservesEntity> getReserves() {
		return reserves;
	}

	public void setReserves(List<BookReservesEntity> reserves) {
		this.reserves = reserves;
	}

	public List<BookReviewsEntity> getReviews() {
		return reviews;
	}

	public void setReviews(List<BookReviewsEntity> reviews) {
		this.reviews = reviews;
	}

	public String getBookImageName() {
		return bookImageName;
	}

	public void setBookImageName(String bookImageName) {
		this.bookImageName = bookImageName;
	}

}
