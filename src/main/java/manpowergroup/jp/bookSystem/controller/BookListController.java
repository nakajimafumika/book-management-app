package manpowergroup.jp.bookSystem.controller;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import manpowergroup.jp.bookSystem.service.BookListExportService;

/**
* 書籍一覧ページを表示するMVCコントローラー。
*  /book/listに対する GET リクエストを処理。
* templates/bookSystem/bookList/book-list.htmlを表示。
* 
*/

@Profile("!mock")
@Controller
@RequestMapping("/book")
public class BookListController {

	private final BookListExportService bookListExportService;

	// コンストラクタでサービスを注入
	public BookListController(BookListExportService bookListExportService) {
		this.bookListExportService = bookListExportService;

	}

	// 書籍一覧を表示する
	@GetMapping("/list")
	public String showBookListPage() {
		return "bookSystem/bookList/book-list";
	}

}
