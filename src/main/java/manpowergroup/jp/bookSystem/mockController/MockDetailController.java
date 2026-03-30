package manpowergroup.jp.bookSystem.mockController;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.Data;

@Controller
@Profile("mock")
@RequestMapping("book")
public class MockDetailController {

	@GetMapping("/detail/{id}")
	public String showDetail(@PathVariable Long id, Model model) {

		// 仮のBookクラスをここで定義
		Book mockBook = new Book();
		mockBook.setId(id);
		mockBook.setTitle("IT基礎");
		mockBook.setAuthor("山田太郎");
		mockBook.setPublisher("架空出版社");
		mockBook.setPublicationDate("2022/5/10");
		mockBook.setLoanStatus("2025/09/18からnakajimafumikaに貸出済み");
		mockBook.setReturnDueDate("2025/9/30");
		mockBook.setReservationStatus("2025/10/30からsumishinjuに貸出予定");
		mockBook.setIsbnCode("978-4-00-000000-0");
		mockBook.setManagementId("BK-00123");
		mockBook.setOid("test");
		mockBook.setReview(BigDecimal.valueOf(1));
		mockBook.setLoans(false);

		// 仮のコメントリスト
		List<Comment> comments = List.of(
				new Comment("面白かった！"),
				new Comment("面白かった！"),
				new Comment("面白かった！"),
				new Comment("面白かった！"),
				new Comment("面白かった！"),
				new Comment("面白かった！"),
				new Comment("面白かった！"),
				new Comment("面白かった！"),
				new Comment("面白かった！"),
				new Comment("面白かった！"),
				new Comment("また読みたいです"));

		mockBook.setComments(comments);
		model.addAttribute("book", mockBook);

		return "bookSystem/bookList/book-detail";
	}

	// 仮のBookクラス（本番では別ファイルに分ける）
	@Data
	static class Book {
		private Long id;
		private String title;
		private String author;
		private String publisher;
		private String publicationDate;
		private String returnDueDate;
		private String loanStatus;
		private String reservationStatus;
		private String isbnCode;
		private String managementId;
		private boolean isLoan;
		private List<Comment> comments;
		private BigDecimal review;
		private boolean loans;
		private String oid;
	}

	// 仮のCommentクラス
	@Data
	static class Comment {
		private String text;

		public Comment(String text) {
			this.text = text;
		}

		public String getText() {
			return text;
		}
	}

}
