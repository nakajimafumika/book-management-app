package manpowergroup.jp.bookSystem.controller;

import java.util.List;

import org.springframework.context.annotation.Profile;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import manpowergroup.jp.bookSystem.dto.LoanStatusDto;
import manpowergroup.jp.bookSystem.dto.ReserveStatusDto;
import manpowergroup.jp.bookSystem.service.BookStatusService;
import manpowergroup.jp.common.login.AppUser;

@Profile("!mock")
@Controller
@RequestMapping("/book")
public class BookStatusController {
	private final BookStatusService bookStatusService;

	public BookStatusController(BookStatusService bookStatusService) {
		this.bookStatusService = bookStatusService;
	}

	@GetMapping("/status")
	public String getBookStatus(@AuthenticationPrincipal AppUser user, Model model) {
		// 貸出一覧
		List<LoanStatusDto> loans = bookStatusService.getLoanStatus(user.getOid());
		if (loans.isEmpty()) {
			model.addAttribute("loanMessage", "現在貸出中の書籍はありません");
		} else {
			model.addAttribute("loanList", loans);
		}

		// 予約一覧
		List<ReserveStatusDto> reserves = bookStatusService.getReserveStatus(user.getOid());
		if (reserves.isEmpty()) {
			model.addAttribute("reserveMessage", "現在予約中の書籍はありません");
		} else {
			model.addAttribute("reserveList", reserves);
		}

		return "bookSystem/bookStatus/book-status"; // book-status.html
	}

}
