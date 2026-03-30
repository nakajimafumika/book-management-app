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
@Table(name = "t_book_reserves")
public class BookReservesEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;

	// --- 外部キー：management_id → m_book_lists.management_id ---
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "management_id", referencedColumnName = "management_id", nullable = false)
	private BookListsEntity book;

	// --- 外部キー：oid → m_users.oid ---
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "oid", referencedColumnName = "oid", nullable = false)
	private MUser user;

	@Column(name = "reserves_date", nullable = false)
	private LocalDate reservesDate;

	@Column(name = "return_due_date", nullable = false)
	private LocalDate returnDueDate;

	@Column(name = "loan_complete_flag", nullable = false)
	private Integer loanCompleteFlag = 0;

	// --- Getter / Setter ---

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public BookListsEntity getBook() {
		return book;
	}

	public void setBook(BookListsEntity book) {
		this.book = book;
	}

	public MUser getUser() {
		return user;
	}

	public void setUser(MUser user) {
		this.user = user;
	}

	public LocalDate getReservesDate() {
		return reservesDate;
	}

	public void setReservesDate(LocalDate reservesDate) {
		this.reservesDate = reservesDate;
	}

	public LocalDate getReturnDueDate() {
		return returnDueDate;
	}

	public void setReturnDueDate(LocalDate returnDueDate) {
		this.returnDueDate = returnDueDate;
	}

	public Integer getLoanCompleteFlag() {
		return loanCompleteFlag;
	}

	public void setLoanCompleteFlag(Integer loanCompleteFlag) {
		this.loanCompleteFlag = loanCompleteFlag;
	}
}
