package manpowergroup.jp.bookSystem.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;
import manpowergroup.jp.bookSystem.validation.ValidPublicationDate;

@Data
public class BookRegisterForm {
	@Pattern(regexp = "^([0-9]{10}|[0-9]{13})$", message = "ISBNコードは半角数字で10桁または13桁で入力してください")
	@NotBlank(message = "ISBNコードは必須です")
	private String isbnCode;

	@NotBlank(message = "管理IDは必須です")
	@Size(max = 20, message = "管理IDは20文字以内で入力してください")
	private String managementId;

	@NotBlank(message = "タイトルは必須です")
	@Size(max = 100, message = "タイトルは100文字以内で入力してください")
	private String title;

	@NotBlank(message = "著者は必須です")
	@Size(max = 100, message = "著者は100文字以内で入力してください")
	private String author;

	@NotBlank(message = "出版社は必須です")
	@Size(max = 30, message = "出版社は30文字以内で入力してください")
	private String publisher;

	@NotBlank(message = "出版日は必須です")
	@ValidPublicationDate
	private String publicationDate;

	private MultipartFile uploadBookImage;

}
