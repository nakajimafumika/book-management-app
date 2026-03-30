package manpowergroup.jp.bookSystem.controller.api;

import java.io.IOException;
import java.util.List;

import jakarta.servlet.http.HttpServletResponse;

import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;
import manpowergroup.jp.bookSystem.dto.BookListDto;
import manpowergroup.jp.bookSystem.service.BookListExportService;
import manpowergroup.jp.bookSystem.service.BookListService;

/**
 * REST APIコントローラー：書籍一覧を取得・出力するエンドポイントを提供します。
 * /api/book-list に対する GET リクエストを処理。
 * 書籍情報のリストを JSON / ファイル / PDF 形式で返します。
 *
 * @see BookListService
 * @see BookListDto
 */
@Slf4j
@RestController
@Profile("!mock")
@RequestMapping("/api")
public class BookListApiController {

	private final BookListService bookListService;
	private final BookListExportService bookListExportService;

	public BookListApiController(BookListService bookListService,
			BookListExportService bookListExportService) {
		this.bookListService = bookListService;
		this.bookListExportService = bookListExportService;
	}

	/** 書籍一覧を JSON で返す */
	@GetMapping("/book-list")
	public List<BookListDto> getBookList() {
		return bookListService.getBookList();
	}

	/** 書籍一覧をファイル出力（CSV/Excel/PDF） */
	@GetMapping("/book-list/export")
	public void exportBooks(@RequestParam String format, HttpServletResponse response) throws IOException {
		try {
			bookListExportService.export(format, response, false); // attachment
		} catch (Exception e) {
			log.error("書籍一覧出力エラー", e);
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
		}
	}

	/** 書籍一覧を PDF 表示（inline） */
	@GetMapping("/book-list/view")
	public void viewBooks(HttpServletResponse response) throws IOException {
		try {
			bookListExportService.export("pdf", response, true); // inline
			//response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "テスト用のランタイムエラー");

		} catch (Exception e) {
			log.error("書籍一覧PDF表示エラー", e);
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
		}
	}
}
