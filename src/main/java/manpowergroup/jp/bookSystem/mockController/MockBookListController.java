package manpowergroup.jp.bookSystem.mockController;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Profile("mock")
@RequestMapping("book")
public class MockBookListController {
	@GetMapping("/list")
	public String showMenu() {
		return "bookSystem/bookList/book-list"; // book-list.html を表示
	}
}