package manpowergroup.jp.bookSystem.controller.api;

import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import manpowergroup.jp.bookSystem.service.BookReturnAndCancelService;

@RestController
@Profile("!mock")
@RequestMapping("/api/book")
public class BookRturnAndCancelApiController {

	private final BookReturnAndCancelService bookReturnAndCancelService;

	public BookRturnAndCancelApiController(BookReturnAndCancelService bookReturnAndCancelService) {
		this.bookReturnAndCancelService = bookReturnAndCancelService;
	}

	@PostMapping("/return/{id}")
	public ResponseEntity<Void> returnBook(@PathVariable Long id) {
		bookReturnAndCancelService.returnBook(id);
		return ResponseEntity.ok().build();
	}

	@PostMapping("/reserve/cancel/{id}")
	public ResponseEntity<Void> cancelReserve(@PathVariable Long id) {
		bookReturnAndCancelService.cancelReserve(id);
		return ResponseEntity.ok().build();
	}
}
