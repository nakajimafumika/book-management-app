package manpowergroup.jp.common.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

	@GetMapping({ "/home", "/" })
	public String showHomePage() {
		return "common/home"; // resources/templates/home.html を表示
	}

}
