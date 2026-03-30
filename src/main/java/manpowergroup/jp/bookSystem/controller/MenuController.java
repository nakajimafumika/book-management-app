package manpowergroup.jp.bookSystem.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("book")
public class MenuController {

	@GetMapping("/menu")
	public String showMenu() {
		return "bookSystem/common/menu"; // menu.html を表示
	}
}
