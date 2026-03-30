package manpowergroup.jp.bookSystem.controller.api;

import java.io.IOException;

import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import manpowergroup.jp.bookSystem.form.BookRegisterForm;
import manpowergroup.jp.bookSystem.service.BookRegisterService;

@RestController
@Profile("!mock")
@RequestMapping("/api")
public class BookRegisterApiController {
	private final BookRegisterService bookRegisterService;

	// コンストラクタ
	public BookRegisterApiController(BookRegisterService bookRegisterService) {
		this.bookRegisterService = bookRegisterService;
	}

	// 書籍情報と写真を登録
	@PostMapping("/book-register")
	public ResponseEntity<Integer> registerBook(
			@ModelAttribute BookRegisterForm form) throws IOException {
		// 書籍情報を保存
		bookRegisterService.registerBook(form);

		return ResponseEntity.ok().build();
	}

	// 本の情報をCSVファイルやExcelで登録
	@PostMapping("/book-register-file")
	public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) throws IOException {
		bookRegisterService.registerBooksFromFile(file);
		return ResponseEntity.ok().build();
	}
}
