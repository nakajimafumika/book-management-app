package manpowergroup.jp.bookSystem.dto;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import lombok.Data;

@Data
public class BookDetailDto {

	// 本の基本情報
	private Integer id;
	private String managementId;
	private String isbnCode;
	private String title;
	private String author;
	private String publisher;
	private String publicationDate;
	private String bookImageName;

	// 貸出情報
	private String loanDate;
	private String returnDueDate;
	private String loanUser;
	private String loanStatus;
	private Boolean loans;

	// 予約情報
	private String reservesDate;
	private String reserveUser;
	private String reserveStatus;
	private Boolean alreadyReserved;

	// レビュー情報
	private Double review;
	private List<String> comments;

	// コンストラクタ
	public BookDetailDto(
			Integer id,
			String managementId,
			String isbnCode,
			String title,
			String author,
			String publisher,
			String rawPublicationDate,
			String bookImageName,

			LocalDate rawLoanDate,
			LocalDate rawReturnDueDate,
			String loanUser,
			Boolean loans,

			LocalDate rawReservesDate,
			String reserveUser,
			Boolean alreadyReserved,

			Double review) {

		// 日付を"yyyy/MM/dd"形式に変換する(これはReservesDateとLoanDateの変換に必要なので消さない）
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");

		this.id = id;
		this.managementId = managementId;
		this.isbnCode = isbnCode;
		this.title = title;
		this.author = author;
		this.publisher = publisher;
		this.publicationDate = rawPublicationDate != null ? rawPublicationDate.replace("-", "/") : null;
		this.bookImageName = bookImageName;

		this.loanDate = rawLoanDate != null ? rawLoanDate.format(formatter) : null;
		this.returnDueDate = rawReturnDueDate != null ? rawReturnDueDate.format(formatter) : null;
		this.loanUser = loanUser;
		this.loans = loanUser != null;

		// 貸出状態の文言
		this.loanStatus = (rawLoanDate != null && loanUser != null)
				? rawLoanDate.format(formatter) + " に " + loanUser + " に貸出済み"
				: "貸出可";

		this.reservesDate = rawReservesDate != null ? rawReservesDate.format(formatter) : null;
		this.reserveUser = reserveUser;
		this.alreadyReserved = alreadyReserved;

		// 予約状態の文言
		this.reserveStatus = (rawReservesDate != null && reserveUser != null)
				? rawReservesDate.format(formatter) + " から " + reserveUser + " に貸出予定"
				: "予約はありません";

		this.review = review != null ? review : 0.0;

	}

}
