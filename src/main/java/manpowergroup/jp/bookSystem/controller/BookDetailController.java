package manpowergroup.jp.bookSystem.controller;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.extern.slf4j.Slf4j;
import manpowergroup.jp.bookSystem.dto.BookDetailDto;
import manpowergroup.jp.bookSystem.service.BookDetailService;

@Slf4j
@Controller
@Profile("!mock")
@RequestMapping("/book")
public class BookDetailController {

	private final BookDetailService bookDetailService;

	public BookDetailController(BookDetailService bookDetailService) {
		this.bookDetailService = bookDetailService;
	}

	@GetMapping("/detail/{id}")
	public String showBookDetail(@PathVariable("id") Integer id, Model model) {
		BookDetailDto bookDetail = bookDetailService.getBookDetailById(id);

		model.addAttribute("book", bookDetail);
		return "bookSystem/bookList/book-detail";
	}
}
