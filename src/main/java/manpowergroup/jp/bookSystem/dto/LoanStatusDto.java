package manpowergroup.jp.bookSystem.dto;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import lombok.Data;

@Data
public class LoanStatusDto {
	private Integer loanId;
	private String title; // 書籍名
	private String returnDueDate; // 返却日（フォーマット済み文字列）

	public LoanStatusDto(Integer loanId, String title, LocalDate returnDueDate) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
		this.loanId = loanId;
		this.title = title;
		this.returnDueDate = returnDueDate.format(formatter);
	}
}
