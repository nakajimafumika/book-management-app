package manpowergroup.jp.bookSystem.form;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class BookLoanForm {
	private String managementId;
	@JsonFormat(pattern = "yyyy-MM-dd")
	private LocalDate loanDate;
	@JsonFormat(pattern = "yyyy-MM-dd")
	private LocalDate returnDueDate;
}
