package manpowergroup.jp.bookSystem.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import lombok.Data;

@Data
public class BookRequestForm {
	@Pattern(regexp = "^([0-9]{10}|[0-9]{13})$", message = "ISBNコードは半角数字で10桁または13桁で入力してください")
	@NotBlank(message = "ISBNコードは必須です")
	private String isbnCode;

	@NotBlank(message = "タイトルは必須です")
	@Size(max = 100, message = "タイトルは100文字以内で入力してください")
	private String title;

	@NotBlank(message = "著者は必須です")
	@Size(max = 100, message = "著者は100文字以内で入力してください")
	private String author;

	@Size(max = 255, message = "urlは255文字以内で入力してください")
	private String url;

	@Size(max = 200, message = "メモは200文字以内で入力してください")
	private String memo;
}
