package manpowergroup.jp.bookSystem.controller;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Profile("!mock")
@Controller
@RequestMapping("/book")
public class BookRequestController {

	@GetMapping("/request")
	public String ShowRequestPage() {
		return "bookSystem/bookRequest/book-request";
	}
}
