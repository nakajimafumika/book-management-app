package manpowergroup.jp.bookSystem.controller.api;

import jakarta.validation.Valid;

import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import manpowergroup.jp.bookSystem.form.BookRequestForm;
import manpowergroup.jp.bookSystem.service.BookRequestService;
import manpowergroup.jp.common.login.AppUser;

@RestController
@Profile("!mock")
@RequestMapping("/api")
public class bookRequestApiController {

	private final BookRequestService bookRequestService;

	public bookRequestApiController(BookRequestService bookRequestService) {
		this.bookRequestService = bookRequestService;
	}

	@PostMapping("/book-request")
	public ResponseEntity<String> BookRegister(@Valid @RequestBody BookRequestForm request,
			@AuthenticationPrincipal AppUser user) {

		bookRequestService.registerBookRequest(request, user.getOid());

		return ResponseEntity.ok().build();
	}

}
