package manpowergroup.jp.bookSystem.controller.api;

import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;
import manpowergroup.jp.bookSystem.dto.BookReserveDto;
import manpowergroup.jp.bookSystem.form.BookReserveForm;
import manpowergroup.jp.bookSystem.service.BookReserveRequestService;
import manpowergroup.jp.bookSystem.service.BookReserveService;
import manpowergroup.jp.common.login.AppUser;

@Slf4j
@RestController
@Profile("!mock")
@RequestMapping("/api")
public class BookReserveApiController {
	private final BookReserveService bookReserveService;
	private final BookReserveRequestService bookReserveRequestService;

	public BookReserveApiController(BookReserveService bookReserveService,
			BookReserveRequestService bookReserveRequestService) {
		this.bookReserveService = bookReserveService;
		this.bookReserveRequestService = bookReserveRequestService;
	}

	//予約情報取得用コントローラー
	@GetMapping("/reserve-info")
	public BookReserveDto getReserveInfo(@RequestParam String managementId,
			@AuthenticationPrincipal AppUser user) {
		String oid = user.getOid();

		BookReserveDto dto = bookReserveService.getReserveAndLoanDates(managementId, oid);
		return dto;
	}

	// 予約処理
	@PostMapping("/reserve-register")
	public ResponseEntity<?> registerReserve(@RequestBody BookReserveForm request,
			@AuthenticationPrincipal AppUser user) {

		bookReserveRequestService.reserveRequest(request, user.getOid());
		return ResponseEntity.ok().build();
	}

}