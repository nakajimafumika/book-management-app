package manpowergroup.jp.bookSystem.controller.api;

import java.time.LocalDate;

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
import manpowergroup.jp.bookSystem.dto.BookLoanDto;
import manpowergroup.jp.bookSystem.form.BookLoanForm;
import manpowergroup.jp.bookSystem.service.BookLoanRequestService;
import manpowergroup.jp.bookSystem.service.BookLoanService;
import manpowergroup.jp.common.login.AppUser;

@Slf4j
@RestController
@Profile("!mock")
@RequestMapping("/api")
public class BookLoanApiController {
	private final BookLoanService bookLoanService;
	private final BookLoanRequestService bookLoanRequestService;

	// コンストラクタ
	public BookLoanApiController(BookLoanService bookLoanService, BookLoanRequestService bookLoanRequestService) {
		this.bookLoanService = bookLoanService;
		this.bookLoanRequestService = bookLoanRequestService;
	}

	//予約情報取得用コントローラー
	@GetMapping("/loan-info")
	public BookLoanDto getLoanInfo(@RequestParam String managementId,
			@AuthenticationPrincipal AppUser user) {
		String oid = user.getOid();

		BookLoanDto dto = bookLoanService.getReservedDates(managementId, oid);
		return dto;
	}

	// 貸出処理
	@PostMapping("/loan-register")
	public ResponseEntity<?> registerLoan(@RequestBody BookLoanForm request,
			@AuthenticationPrincipal AppUser user) {

		// 貸出開始日を現在日に設定
		request.setLoanDate(LocalDate.now());

		// ログインユーザーの oid をサービスに渡す
		bookLoanRequestService.registerLoan(request, user.getOid());
		return ResponseEntity.ok().build();
	}
}
